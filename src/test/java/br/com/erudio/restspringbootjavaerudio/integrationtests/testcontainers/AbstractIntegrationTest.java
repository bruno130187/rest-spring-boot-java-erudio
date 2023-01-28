package br.com.erudio.restspringbootjavaerudio.integrationtests.testcontainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static Network network = Network.newNetwork();
        static MSSQLServerContainer<?> mssqlServerContainer = new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2019-latest")
                .acceptLicense()
                .withNetwork(network)
                .withExposedPorts(1433, 1433)
                .withInitScript("db/mssql-test.sql")
                .waitingFor(Wait.forLogMessage("*SQL Server is now ready for client connections*", 1))
                ;

        private static void startContainers() {
            Startables.deepStart(Stream.of(mssqlServerContainer)).join();
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", mssqlServerContainer.getJdbcUrl().concat(";databaseName=rest-spring-boot-erudio;encrypt=false;trustServerCertificate=true;serverTimezone=America/Sao_Paulo"),
                    "spring.datasource.username", mssqlServerContainer.getUsername(),
                    "spring.datasource.password", "A_Str0ng_Required_Password"
            );
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testcontainers = new MapPropertySource(
                    "testcontainers",
                    (Map) createConnectionConfiguration());
            environment.getPropertySources().addFirst(testcontainers);
            System.out.println(testcontainers);
            System.out.println("Waiting 5 seconds!");
            wait(5000);
        }

        public static void wait(int ms) {
            try {
                Thread.sleep(ms);
            }
            catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

    }

}
