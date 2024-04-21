package com.flytrap.rssreader.api.auth.presentation.dto;


import com.flytrap.rssreader.api.account.domain.Account;

public record SignInResponse(
    long id,
    String name,
    String profile
) {

    public static SignInResponse from(Account member) {
        return new SignInResponse(member.getId().value(), member.getName().value(), member.getProfile());
    }
}
