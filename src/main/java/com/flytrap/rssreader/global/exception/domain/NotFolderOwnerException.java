package com.flytrap.rssreader.global.exception.domain;

import com.flytrap.rssreader.global.model.DefaultDomain;

/**
 * 로그인한 멤버가 폴더의 주인이 아닐때 발생 시킬 에러
 */
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
