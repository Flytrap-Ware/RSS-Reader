package com.flytrap.rssreader.api.account.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AuthProvider;
import com.flytrap.rssreader.api.account.infrastructure.repository.AccountCommandImplementation;
import com.flytrap.rssreader.api.account.infrastructure.repository.AccountQueryImplementation;
import com.flytrap.rssreader.fixture.FixtureFactory;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountQueryImplementation accountQueryImplementation;
    @Mock
    AccountCommandImplementation accountCommandImplementation;
    @InjectMocks
    AccountService accountService;

    @Test
    @DisplayName("존재하는 회원으로 로그인에 성공한다")
    void login_success() {
        //given
        when(accountQueryImplementation.readByProviderKey(anyLong()))
                .thenReturn(Optional.of(FixtureFactory.generateAccount()));

        OAuthUserResourceMock userResource = new OAuthUserResourceMock(1L, "test@test.com", "name", "test.com");

        //when
        var account = accountService.login(userResource);

        //then
        verify(accountQueryImplementation).readByProviderKey(anyLong());
        verify(accountCommandImplementation, never()).create(any());

        assertThat(account).isNotNull();
        assertThat(account.getId().value()).isEqualTo(1L);
        assertThat(account.getName().value()).isEqualTo("name");
    }

    @Test
    @DisplayName("새로운 회원으로 회원가입과 로그인에 성공한다")
    void login_new() {
        //given
        when(accountQueryImplementation.readByProviderKey(anyLong())).thenReturn(Optional.empty());
        when(accountCommandImplementation.create(any()))
                .thenReturn(FixtureFactory.generateAccount());

        OAuthUserResourceMock userResource = new OAuthUserResourceMock(1L, "test@gmail.com", "name", "https://avatarUrl.jpg");

        //when
        var account = accountService.login(userResource);

        //then
        verify(accountQueryImplementation).readByProviderKey(anyLong());
        verify(accountCommandImplementation).create(any(Account.class));

        assertThat(account).isNotNull();
        assertThat(account.getId().value()).isEqualTo(1L);
        assertThat(account.getName().value()).isEqualTo("name");
        assertThat(account.getEmail()).isEqualTo("test@gmail.com");
        assertThat(account.getProfile()).isEqualTo("https://avatarUrl.jpg");
        assertThat(account.getAuthProvider()).isEqualTo(AuthProvider.GITHUB);
        assertThat(account.getProviderKey()).isEqualTo(1L);
    }
}
