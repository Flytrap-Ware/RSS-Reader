package com.flytrap.rssreader.api.auth.presentation.controller;

import com.flytrap.rssreader.api.auth.presentation.controller.swagger.AuthControllerApi;
import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.LoginRequest;
import com.flytrap.rssreader.api.auth.presentation.dto.LoginResponse;
import com.flytrap.rssreader.api.auth.business.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthControllerApi {

    private final AuthService authService;

    @PostMapping("/login")
    public ApplicationResponse<LoginResponse> login(@RequestBody LoginRequest request,
                                                      HttpSession session) {

        Account account = authService.doAuthentication(request);
        authService.login(account, session);
        return new ApplicationResponse<>(LoginResponse.from(account));
    }

    @Override
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApplicationResponse<Void> logout(HttpSession session) {

        authService.signOut(session);
        return new ApplicationResponse<>(null);
    }
}
