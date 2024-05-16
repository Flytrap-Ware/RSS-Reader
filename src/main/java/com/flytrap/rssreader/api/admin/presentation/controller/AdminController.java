package com.flytrap.rssreader.api.admin.presentation.controller;

import com.flytrap.rssreader.api.admin.business.service.AdminSystemService;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.auth.presentation.dto.LoginRequest;
import com.flytrap.rssreader.api.auth.presentation.dto.LoginResponse;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.AdminLogin;
import com.flytrap.rssreader.global.properties.AdminProperties;
import com.flytrap.rssreader.global.properties.AuthProperties;
import jakarta.servlet.http.HttpSession;
import javax.security.sasl.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminProperties adminProperties;
    private final AuthProperties authProperties;
    private final AdminSystemService adminSystemService;

    @PostMapping("/api/admin/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<LoginResponse> getAdminProperties(
        @RequestBody LoginRequest request, HttpSession session
    ) throws AuthenticationException {

        if (request.code().equals(adminProperties.code())) {
            session.setAttribute(authProperties.sessionId(),
                AccountCredentials.from(adminProperties.getAccount()));
            log.info("🙌 admin login success");

            return new ApplicationResponse<>(
                new LoginResponse(
                    adminProperties.memberId(),
                    adminProperties.memberName(),
                    adminProperties.memberProfile()
                )
            );
        } else {
            throw new AuthenticationException("admin login fail");
        }
    }

    @PatchMapping("/api/admin/post-collection/start")
    public ApplicationResponse<String> startPostCollect(
        @AdminLogin AccountCredentials accountCredentials
    ) {
        adminSystemService.startPostCollection();

        return new ApplicationResponse<>("게시글 수집 기능 시작");
    }

    @PatchMapping("/api/admin/post-collection/stop")
    public ApplicationResponse<String> stopPostCollect(
        @AdminLogin AccountCredentials accountCredentials
    ) {
        adminSystemService.stopPostCollection();

        return new ApplicationResponse<>("게시글 수집 기능 중지");
    }

}
