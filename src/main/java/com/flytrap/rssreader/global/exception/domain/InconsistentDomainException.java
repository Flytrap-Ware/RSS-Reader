package com.flytrap.rssreader.global.exception.domain;

/**
 * Root Domain과 Sub Domain을 조합할 때 정합성이 일치하지 않을 경우 발생하는 예외.
 */
public class InconsistentDomainException extends ApplicationException {

    static {
        message = "💣 Domain inconsistency detected = %s";
    }

    public InconsistentDomainException(Class<?> domainClass) {
        super(domainClass);
    }
}