package com.flytrap.rssreader.global.presentation.resolver;

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
public class AuthorizationArgumentResolver implements HandlerMethodArgumentResolver { // 이것도 presentation에서

    private final AuthorizationContext context;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Login parameterAnnotation = parameter.getParameterAnnotation(Login.class);

        if (parameterAnnotation != null && parameterAnnotation.required()) {
            return context.getLoginMember().orElseThrow(() -> new AuthenticationException("로그인이 필요한 기능입니다."));
        }

        return context.getLoginMember().get();
    }
}
