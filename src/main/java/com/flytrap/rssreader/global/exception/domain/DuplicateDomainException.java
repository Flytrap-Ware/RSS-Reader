package com.flytrap.rssreader.global.exception.domain;

import com.flytrap.rssreader.global.model.DefaultDomain;

public class DuplicateDomainException extends ApplicationException {

    static {
        message = "💣 Duplicate domain = %s";
    }

    public DuplicateDomainException(DefaultDomain domain) {
        super(
            domain.getDomainCodeWithId(),
            String.format(message.formatted(domain.getDomainCodeWithId()))
        );
    }

    public DuplicateDomainException(Class<? extends DefaultDomain> domainClass) {
        super(domainClass);
    }
}
