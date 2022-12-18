package br.com.erudio.restspringbootjavaerudio.exceptions;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
public class ExceptionResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Date timestamp;
    private final String message;
    private final String details;

    public ExceptionResponse(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}
