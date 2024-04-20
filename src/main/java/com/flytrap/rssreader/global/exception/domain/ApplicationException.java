package com.flytrap.rssreader.global.exception.domain;

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

    // TODO: DefaultDomain이 통합되면 파라미터 타입 Class<? extends DefaultDomain>으로 변경하기
    public ApplicationException(Class<?> domain) {
        super(String.format(message, domain.getSimpleName()));
        this.defaultCode = domain.getSimpleName();
    }

    public String getDefaultCode() {
        return defaultCode;
    }

}
