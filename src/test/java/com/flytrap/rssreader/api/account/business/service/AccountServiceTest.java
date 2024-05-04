package com.flytrap.rssreader.api.account.business.service;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AuthProvider;
import com.flytrap.rssreader.api.account.infrastructure.repository.AccountQueryImplementation;
import com.flytrap.rssreader.fixture.FixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountQueryImplementation accountQueryImplementation;
    @InjectMocks
    AccountService accountService;

    @Test
    @DisplayName("존재하는 회원으로 로그인에 성공한다")
    void signin_success() {
        //given
        when(accountQueryImplementation.readByProviderKey(anyLong()))
                .thenReturn(Optional.of(FixtureFactory.generateAccount()));

        OAuthOAuthUserResourceMock userResource = new OAuthOAuthUserResourceMock(1L, "test@test.com", "name", "test.com");

        //when
        var account = accountService.signIn(userResource);

        //then
        verify(accountQueryImplementation).readByProviderKey(anyLong());
        verify(accountQueryImplementation, never()).save(any());

        assertThat(account).isNotNull();
        assertThat(account.getId().value()).isEqualTo(1L);
        assertThat(account.getName().value()).isEqualTo("name");
    }

    @Test
    @DisplayName("새로운 회원으로 회원가입과 로그인에 성공한다")
    void signin_new() {
        //given
        when(accountQueryImplementation.readByProviderKey(anyLong())).thenReturn(Optional.empty());
        when(accountQueryImplementation.save(any()))
                .thenReturn(FixtureFactory.generateAccount());

        OAuthOAuthUserResourceMock userResource = new OAuthOAuthUserResourceMock(1L, "test@gmail.com", "name", "https://avatarUrl.jpg");

        //when
        var account = accountService.signIn(userResource);

        //then
        verify(accountQueryImplementation).readByProviderKey(anyLong());
        verify(accountQueryImplementation).save(any(Account.class));

        assertThat(account).isNotNull();
        assertThat(account.getId().value()).isEqualTo(1L);
        assertThat(account.getName().value()).isEqualTo("name");
        assertThat(account.getEmail()).isEqualTo("test@gmail.com");
        assertThat(account.getProfile()).isEqualTo("https://avatarUrl.jpg");
        assertThat(account.getAuthProvider()).isEqualTo(AuthProvider.GITHUB);
        assertThat(account.getProviderKey()).isEqualTo(1L);
    }
}
