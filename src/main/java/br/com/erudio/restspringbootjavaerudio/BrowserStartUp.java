package br.com.erudio.restspringbootjavaerudio;
import java.awt.Desktop;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BrowserStartUp {

    private static int port = 8090;

    @EventListener(ApplicationReadyEvent.class)
    public static void startBrowser() {
        String url = "http://localhost:" + port + "/swagger-ui/index.html";
        try {
            if (Desktop.isDesktopSupported()) {
                // java with desktop support
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(new URI(url));
            } else {
                // no java desktop support
                // fall back into dark ages
                String osName = System.getProperty("os.name");

                if (osName.toLowerCase().contains("linux")) {
                    // probably linux/unix
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec("xdg-open " + url);
                } else if (osName.toLowerCase().contains("windows")) {
                    // older windows
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                } else if (osName.toLowerCase().contains("mac")) {
                    // probably mac os
                    Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL", String.class).invoke(null,
                            url);
                }
            }
        } catch (IOException | URISyntaxException | IllegalAccessException | IllegalArgumentException
                 | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            log.error("Could not start browser!", e);
            e.printStackTrace(); // NOSONAR the user has to see it
        }
    }
}