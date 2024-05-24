package com.flytrap.rssreader.global.exception;

import com.flytrap.rssreader.global.exception.domain.ApplicationException;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import com.flytrap.rssreader.global.exception.domain.NotBelongToMemberException;
import com.flytrap.rssreader.global.model.ErrorResponse;
import java.util.Objects;
import javax.security.sasl.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
        DuplicateKeyException.class, IllegalArgumentException.class})
    public ErrorResponse handleBadInputException(RuntimeException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ErrorResponse.occur("input error", e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorResponse handleBadInputException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ErrorResponse.occur(Objects.requireNonNull(e.getFieldError()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchDomainException.class)
    public ErrorResponse handleBadInputException(NoSuchDomainException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ErrorResponse.occur(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponse handleException(BindException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ErrorResponse.occur(Objects.requireNonNull(e.getFieldError()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class})
    public ErrorResponse handleAuthException(RuntimeException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ErrorResponse.occur("login", e);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({ForbiddenAccessFolderException.class, NotBelongToMemberException.class})
    public ErrorResponse handleForbiddenException(RuntimeException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ErrorResponse.occur("login", e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ApplicationException.class)
    public ErrorResponse handleException(ApplicationException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ErrorResponse.occur(e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ErrorResponse.occur(e);
    }
}
