package com.flytrap.rssreader.global.exception.domain;

import com.flytrap.rssreader.global.model.DefaultDomain;

public class NotFolderOwnerException extends ApplicationException {

    static {
        message = "💣 You are not the owner of the folder = %s";
    }

    public NotFolderOwnerException(DefaultDomain domain) {
        super(
            domain.getDomainCodeWithId(),
            String.format(message.formatted(domain.getDomainCodeWithId()))
        );
    }

    public NotFolderOwnerException(Class<? extends DefaultDomain> domainClass) {
        super(domainClass);
    }

}
