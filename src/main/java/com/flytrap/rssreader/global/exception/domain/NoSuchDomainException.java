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

    public NoSuchDomainException(Class<? extends DefaultDomain> domainClass) {
        super(domainClass);
    }

}
