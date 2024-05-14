package com.flytrap.rssreader.api.auth.business.service;

import com.flytrap.rssreader.api.account.business.service.AccountService;
import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.auth.infrastructure.external.provider.AuthProvider;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.global.properties.AuthProperties;
import com.flytrap.rssreader.api.auth.presentation.dto.Login;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthProperties authProperties;
    private final AuthProvider authProvider;
    private final AccountService accountService;

    public Account doAuthentication(Login request) {
        return authProvider.requestAccessToken(request.code())
                .flatMap(authProvider::requestUserResource)
                .map(accountService::login)
                .block();
    }

    public void login(Account account, HttpSession session) {
        session.setAttribute(authProperties.sessionId(), AccountCredentials.from(account));
    }

    public void signOut(HttpSession session) {
        session.invalidate();
    }
}
