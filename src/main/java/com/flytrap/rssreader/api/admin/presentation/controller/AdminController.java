package com.flytrap.rssreader.api.admin.presentation.controller;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.properties.AdminProperties;
import com.flytrap.rssreader.global.properties.AuthProperties;
import com.flytrap.rssreader.api.auth.presentation.dto.LoginRequest;
import com.flytrap.rssreader.api.auth.presentation.dto.LoginResponse;
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
public class AdminController {

    private final AdminProperties properties;
    private final AuthProperties authProperties;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<LoginResponse> getAdminProperties(
        @RequestBody LoginRequest request, HttpSession session
    ) throws AuthenticationException {

        if (request.code().equals(properties.code())) {
            session.setAttribute(authProperties.sessionId(),
                AccountCredentials.from(properties.getAccount()));
            log.info("🙌 admin login success");

            return new ApplicationResponse<>(
                new LoginResponse(
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
