package com.flytrap.rssreader.global.exception.domain;

public class NoSuchBlogPlatformException extends ApplicationException {

    static {
        message = "💣 No such BlogPlatform  = %s";
    }

    public NoSuchBlogPlatformException(String link) {
        super(
                //TODO: 에러코드 달 것
                link,
                String.format(message.formatted(link))
        );
    }
}
