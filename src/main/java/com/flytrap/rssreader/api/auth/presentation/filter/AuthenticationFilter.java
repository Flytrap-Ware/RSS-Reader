package com.flytrap.rssreader.api.auth.presentation.filter;

import com.flytrap.rssreader.global.properties.AuthProperties;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.global.presentation.resolver.AuthorizationContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(3)
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthProperties authProperties;
    private final AuthorizationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        AccountCredentials attribute = (session != null)
                ? (AccountCredentials) session.getAttribute(authProperties.sessionId())
                : null;
        context.setLoginMember(attribute);

        chain.doFilter(request, response);
    }
}
