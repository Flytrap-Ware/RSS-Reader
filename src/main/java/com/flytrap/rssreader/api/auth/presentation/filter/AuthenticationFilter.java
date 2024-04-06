package com.flytrap.rssreader.api.auth.presentation.filter;

import com.flytrap.rssreader.global.properties.AuthProperties;
import com.flytrap.rssreader.api.auth.presentation.dto.SessionMember;
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
    // TODO: 통합테스트 해보면 좋겠습니다. 필터는 presectation은? 어느 범주로 하는게 좋을지... 빼는게 낫겠죠?
    // 이게 의존성에 따라서 해봐도 될거같아요.

    private final AuthProperties authProperties;
    private final AuthorizationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        SessionMember attribute = (session != null)
            ? (SessionMember) session.getAttribute(authProperties.sessionId())
            : null;
        context.setLoginMember(attribute);

        chain.doFilter(request, response);
    }
}
