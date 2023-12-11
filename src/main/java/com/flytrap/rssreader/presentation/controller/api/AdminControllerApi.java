package com.flytrap.rssreader.presentation.controller.api;

import com.flytrap.rssreader.presentation.dto.Login;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpSession;
import javax.security.sasl.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;

public interface AdminControllerApi {

    @Hidden
    void getAdminProperties(@RequestBody Login request, HttpSession session)
        throws AuthenticationException;
}
