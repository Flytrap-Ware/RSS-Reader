package com.flytrap.rssreader.api.auth.presentation.controller;

import com.flytrap.rssreader.api.auth.presentation.controller.swagger.AdminControllerApi;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.properties.AdminProperties;
import com.flytrap.rssreader.global.properties.AuthProperties;
import com.flytrap.rssreader.api.auth.presentation.dto.Login;
import com.flytrap.rssreader.api.auth.presentation.dto.SignInResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import jakarta.servlet.http.HttpSession;
import javax.security.sasl.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController implements AdminControllerApi {

    private final AdminProperties properties;
    private final AuthProperties authProperties;

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<SignInResponse> getAdminProperties(@RequestBody Login request, HttpSession session)
            throws AuthenticationException {

        if (request.code().equals(properties.code())) {
            session.setAttribute(authProperties.sessionId(), AccountCredentials.from(properties.getMember()));
            log.info("🙌 admin login success");

            return new ApplicationResponse<>(
                new SignInResponse(
                    properties.memberId(),
                    properties.memberName(),
                    properties.memberProfile()
                )
            );
        } else {
            throw new AuthenticationException("admin login fail");
        }
    }

}
