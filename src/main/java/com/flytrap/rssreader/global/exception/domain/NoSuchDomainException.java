package com.flytrap.rssreader.global.exception.domain;

import com.flytrap.rssreader.global.model.DefaultDomain;

public class NoSuchDomainException extends ApplicationException {

    static {
        message = "💣 No such domain = %s";
    }

    public NoSuchDomainException(DefaultDomain domain) {
        super(
                domain.getDomainCodeWithId(),
                String.format(message.formatted(domain.getDomainCodeWithId()))
        );
    }

    // TODO: DefaultDomain이 통합되면 파라미터 타입 Class<? extends DefaultDomain>으로 변경하기
    public NoSuchDomainException(Class<?> domainClass) {
        super(domainClass);
    }

}
