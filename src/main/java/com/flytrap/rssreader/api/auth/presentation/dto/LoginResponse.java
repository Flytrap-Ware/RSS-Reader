package com.flytrap.rssreader.api.auth.presentation.dto;


import com.flytrap.rssreader.api.account.domain.Account;

public record LoginResponse(
    long id,
    String name,
    String profile
) {

    public static LoginResponse from(Account member) {
        return new LoginResponse(member.getId().value(), member.getName().value(), member.getProfile());
    }
}
