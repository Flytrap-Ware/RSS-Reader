package com.flytrap.rssreader.global.presentation.resolver;

import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import javax.security.sasl.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AdminAuthorizationArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthorizationContext context;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter
            .hasParameterAnnotation(AdminLogin.class);
        boolean hasMemberType = AccountCredentials.class
            .isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    ) throws Exception {

        return context.getAccountCredentials()
            .filter(AccountCredentials::isAdmin)
            .orElseThrow(() -> new AuthenticationException("Admin 권한이 필요합니다."));
    }
}
