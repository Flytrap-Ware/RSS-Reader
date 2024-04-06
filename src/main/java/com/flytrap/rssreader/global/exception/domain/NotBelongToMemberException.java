package com.flytrap.rssreader.global.exception.domain;

import com.flytrap.rssreader.global.model.DefaultDomain;

/**
 * Domain이 로그인한 멤버가 수정할 수 있는 권한이 없을 때 에러
 */
public class NotBelongToMemberException extends ApplicationException {

    static {
        message = "💣 Not Belong to Login Member = %s";
    }

    public NotBelongToMemberException(DefaultDomain domain) {
        super(
                domain.getDomainCodeWithId(),
                String.format(message.formatted(domain.getDomainCodeWithId()))
        );
    }

    public NotBelongToMemberException(Class<? extends DefaultDomain> domainClass) {
        super(domainClass);
    }
}
