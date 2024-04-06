package com.flytrap.rssreader.global.model;

import com.flytrap.rssreader.global.exception.domain.ApplicationException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class ErrorResponse {
    private final String errorCode;
    private final String message;

    @Builder
    private ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ErrorResponse occur(String errorCode, Exception exception) {
        return new ErrorResponse(errorCode, exception.getMessage());
    }

    public static ErrorResponse occur(ApplicationException exception) {
        return new ErrorResponse(exception.getDefaultCode(), exception.getMessage());
    }

    public static ErrorResponse occur(FieldError fieldError) {
        return new ErrorResponse(String.format("Input_FieldError_%s", fieldError.getField()), fieldError.getDefaultMessage());
    }

    public static ErrorResponse occur(Exception ex) {
        return new ErrorResponse(null ,ex.getMessage());
    }

    @Override
    public String toString() {
        return String.format("{%n" +
                "\"errorCode\":\"%s\",%n" +
                "\"message\":\"%s\"%n" +
                "}", errorCode, message);
    }

}
