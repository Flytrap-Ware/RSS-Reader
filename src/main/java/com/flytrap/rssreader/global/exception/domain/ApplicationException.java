package com.flytrap.rssreader.global.exception.domain;

import com.flytrap.rssreader.global.model.DefaultDomain;

public class ApplicationException extends RuntimeException {

    private String defaultCode;
    protected static String message;

    static {
        message = "ApplicationException: %s";
    }

    public ApplicationException(String defaultCode, String message) {
        super(message);
        this.defaultCode = defaultCode;
    }

    public ApplicationException(Class<? extends DefaultDomain> domain) {
        super(String.format(message, domain.getSimpleName()));
        this.defaultCode = domain.getSimpleName();
    }

    public String getDefaultCode() {
        return defaultCode;
    }

}
