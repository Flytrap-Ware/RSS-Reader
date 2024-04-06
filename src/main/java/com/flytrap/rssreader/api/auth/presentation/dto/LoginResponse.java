package com.flytrap.rssreader.api.auth.presentation.dto;


import com.flytrap.rssreader.api.member.domain.Member;

public record LoginResponse(
    long id,
    String name,
    String profile
) {

    public static LoginResponse from(Member member) {
        return new LoginResponse(member.getId(), member.getName(), member.getProfile());
    }
}
