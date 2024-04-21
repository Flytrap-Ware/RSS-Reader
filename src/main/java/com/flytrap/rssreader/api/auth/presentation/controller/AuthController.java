package com.flytrap.rssreader.api.auth.presentation.controller;

import com.flytrap.rssreader.api.auth.presentation.controller.swagger.AuthControllerApi;
import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.Login;
import com.flytrap.rssreader.api.auth.presentation.dto.SignInResponse;
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

    @PostMapping("/signin")
    public ApplicationResponse<SignInResponse> signIn(@RequestBody Login request,
                                                      HttpSession session) {

        Account account = authService.doAuthentication(request);
        authService.signIn(account, session);
        return new ApplicationResponse<>(SignInResponse.from(account));
    }

    @Override
    @PostMapping("/signout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApplicationResponse<Void> signOut(HttpSession session) {

        authService.signOut(session);
        return new ApplicationResponse<>(null);
    }
}
