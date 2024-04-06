package com.flytrap.rssreader.service;


import static com.flytrap.rssreader.fixture.FixtureFactory.generateMember;
import static com.flytrap.rssreader.fixture.FixtureFactory.generateUserResource;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.when;

import com.flytrap.rssreader.api.auth.business.service.AuthService;
import com.flytrap.rssreader.api.member.business.service.MemberService;
import com.flytrap.rssreader.api.member.domain.Member;
import com.flytrap.rssreader.fixture.FixtureFields.MemberFields;
import com.flytrap.rssreader.api.auth.infrastructure.external.provider.AuthProvider;
import com.flytrap.rssreader.api.auth.infrastructure.external.dto.AccessToken;
import com.flytrap.rssreader.global.properties.AuthProperties;
import com.flytrap.rssreader.api.auth.presentation.dto.Login;
import com.flytrap.rssreader.api.auth.presentation.dto.SessionMember;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@DisplayName("Auth 서비스 로직 단위 테스트")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    AuthProvider authProvider;

    @Mock
    MemberService memberService;

    @Mock
    HttpSession session;

    @Mock
    AuthProperties authProperties;

    @InjectMocks
    AuthService authService;

    @BeforeEach
    void init() {
        when(authProvider.requestAccessToken(anyString()))
                .thenReturn(Mono.just(new AccessToken("test_access_token", "Bearer")));
        when(authProvider.requestUserResource(any()))
            .thenReturn(Mono.just(generateUserResource()));
        when(memberService.loginMember(any()))
            .thenReturn(generateMember());
    }

    @Test
    @DisplayName("클라이언트가 oauth 인증 서버로부터 받은 코드를 통해 회원가입하거나 로그인할 수 있다.")
    void doAuthentication() {
        // when
        Member member = authService.doAuthentication(new Login("code"));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(member.getId()).isEqualTo(MemberFields.id);
            softAssertions.assertThat(member.getEmail()).isEqualTo(MemberFields.email);
            softAssertions.assertThat(member.getProfile()).isEqualTo(MemberFields.profile);
        });
    }

    @Test
    @DisplayName("클라이언트가 oauth 인증 서버로부터 받은 코드를 통해 세션에 member를 저장된다.")
    void login() {
        when(authProperties.sessionId()).thenReturn("test_session::");
        //given
        Member member = authService.doAuthentication(new Login("code"));
        SessionMember sessionMember = SessionMember.from(member);

        //when
        authService.login(member, session);

        //then
        verify(session, times(1)).setAttribute("test_session::", sessionMember);
    }

    @Test
    @DisplayName("클라이언트가 oauth 인증 서버로부터 받은 코드를 통해 세션에 member를 삭제된다.")
    void logout() {
        //given
        Member member = authService.doAuthentication(new Login("code"));

        //when
        authService.logout(session);

        //then
        verify(session, times(1)).invalidate();
    }
}
