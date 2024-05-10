package com.flytrap.rssreader.api.auth.presentation.controller.swagger;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.Login;
import com.flytrap.rssreader.api.auth.presentation.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpSession;
import javax.security.sasl.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;

public interface AdminControllerApi {

    @Hidden
    ApplicationResponse<LoginResponse> getAdminProperties(@RequestBody Login request, HttpSession session)
        throws AuthenticationException;
}
