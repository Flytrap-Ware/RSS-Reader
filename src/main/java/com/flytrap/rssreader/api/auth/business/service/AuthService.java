package com.flytrap.rssreader.api.auth.business.service;

import com.flytrap.rssreader.api.member.business.service.MemberService;
import com.flytrap.rssreader.api.member.domain.Member;
import com.flytrap.rssreader.api.auth.infrastructure.external.provider.AuthProvider;
import com.flytrap.rssreader.global.properties.AuthProperties;
import com.flytrap.rssreader.api.auth.presentation.dto.Login;
import com.flytrap.rssreader.api.auth.presentation.dto.SessionMember;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthProperties authProperties;

    private final AuthProvider authProvider;
    private final MemberService memberService;

    public Member doAuthentication(Login request) {
        return authProvider.requestAccessToken(request.code())
                .flatMap(authProvider::requestUserResource)
                .map(memberService::loginMember)
                .block();
    }

    public void login(Member member, HttpSession session) {
        session.setAttribute(authProperties.sessionId(), SessionMember.from(member));
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}
