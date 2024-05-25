package com.flytrap.rssreader.api.shared_member.presentation.contoller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flytrap.rssreader.CustomControllerTest;
import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.account.domain.AccountName;
import com.flytrap.rssreader.api.account.domain.AccountRoll;
import com.flytrap.rssreader.api.account.domain.AuthProvider;
import com.flytrap.rssreader.api.account.infrastructure.implement.AccountCommand;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderCommand;
import com.flytrap.rssreader.api.shared_member.domain.SharedMemberCreate;
import com.flytrap.rssreader.api.shared_member.infrastructure.implementation.SharedMemberCommand;
import com.flytrap.rssreader.api.shared_member.presentation.dto.InviteMemberRequest;
import com.flytrap.rssreader.global.presentation.resolver.AdminAuthorizationArgumentResolver;
import com.flytrap.rssreader.global.presentation.resolver.AuthorizationArgumentResolver;
import java.time.Instant;
import javax.security.sasl.AuthenticationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomControllerTest
class SharedMemberCommandControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthorizationArgumentResolver authorizationArgumentResolver;

    @MockBean
    AdminAuthorizationArgumentResolver adminAuthorizationArgumentResolver;

    @Autowired
    FolderCommand folderCommand;

    @Autowired
    AccountCommand accountCommand;

    @Autowired
    SharedMemberCommand sharedMemberCommand;

    @Nested
    class 폴더에_멤버_초대하기_API {

        @Test
        void 멤버_초대_성공시_201응답을_반환한다() throws Exception {
            // given
            var folderOwner = accountCommand
                .create(Account.builder()
                    .name(new AccountName("폴더 주인")).email("example1@email.com").profile("url")
                    .providerKey(11111).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var anotherAccount = accountCommand
                .create(Account.builder()
                    .name(new AccountName("회원2")).email("example2@email.com").profile("url")
                    .providerKey(22222).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(new AccountId(1L))
                    .build()
                );

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            var request = new InviteMemberRequest(anotherAccount.getId().value());

            // when, then
            mockMvc.perform(post("/api/folders/{folderId}/members", folderAggregate.getId().value())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        }

        @Test
        void 로그인_하지_않은_사용자가_요청시_401응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenThrow(AuthenticationException.class);

            var request = new InviteMemberRequest(2L);

            // when, then
            mockMvc.perform(post("/api/folders/{folderId}/members", 1)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        }

        @Test
        void 폴더의_소유자가_아닐_경우_403응답을_반환한다() throws Exception {
            // given
            var folderOwner = accountCommand
                .create(Account.builder()
                    .name(new AccountName("폴더 주인")).email("example1@email.com").profile("url")
                    .providerKey(11111).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var anotherAccount = accountCommand
                .create(Account.builder()
                    .name(new AccountName("회원2")).email("example2@email.com").profile("url")
                    .providerKey(22222).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var theOtherAccount = accountCommand
                .create(Account.builder()
                    .name(new AccountName("회원3")).email("example3@email.com").profile("url")
                    .providerKey(33333).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(new AccountId(1L))
                    .build()
                );

            var accountCredentials = new AccountCredentials(new AccountId(3L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            var request = new InviteMemberRequest(anotherAccount.getId().value());

            // when, then
            mockMvc.perform(post("/api/folders/{folderId}/members", folderAggregate.getId().value())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        }

        @Test
        void 자기_스스로를_초대한_경우_400응답을_반환한다() throws Exception {
            // given
            var folderOwner = accountCommand
                .create(Account.builder()
                    .name(new AccountName("폴더 주인")).email("example1@email.com").profile("url")
                    .providerKey(11111).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(new AccountId(1L))
                    .build()
                );

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            var request = new InviteMemberRequest(folderOwner.getId().value());

            // when, then
            mockMvc.perform(post("/api/folders/{folderId}/members", folderAggregate.getId().value())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        }

        @Test
        void 이미_초대된_멤버를_초대한_경우_400응답을_반환한다() throws Exception {
            // given
            var folderOwner = accountCommand
                .create(Account.builder()
                    .name(new AccountName("폴더 주인")).email("example1@email.com").profile("url")
                    .providerKey(11111).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var anotherAccount = accountCommand
                .create(Account.builder()
                    .name(new AccountName("회원2")).email("example2@email.com").profile("url")
                    .providerKey(22222).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            var sharedMember = sharedMemberCommand
                .create(new SharedMemberCreate(folderAggregate.getId(), anotherAccount.getId()));

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            var request = new InviteMemberRequest(anotherAccount.getId().value());

            // when, then
            mockMvc.perform(post("/api/folders/{folderId}/members", folderAggregate.getId().value())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class 폴더에서_떠나기_API{
        @Test
        void 폴더_떠나기_성공시_204응답을_반환한다() throws Exception {
            // given
            var folderOwner = accountCommand
                .create(Account.builder()
                    .name(new AccountName("폴더 주인")).email("example1@email.com").profile("url")
                    .providerKey(11111).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var anotherAccount = accountCommand
                .create(Account.builder()
                    .name(new AccountName("회원2")).email("example2@email.com").profile("url")
                    .providerKey(22222).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            var sharedMember = sharedMemberCommand
                .create(new SharedMemberCreate(folderAggregate.getId(), anotherAccount.getId()));

            var accountCredentials = new AccountCredentials(anotherAccount.getId(), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            // when, then
            mockMvc.perform(delete("/api/folders/{folderId}/members/me", folderAggregate.getId().value()))
                .andExpect(status().isNoContent());
        }

        @Test
        void 로그인_하지_않은_사용자가_요청시_401응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenThrow(AuthenticationException.class);

            var request = new InviteMemberRequest(2L);

            // when, then
            mockMvc.perform(delete("/api/folders/{folderId}/members/me", 1)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        }

        @Test
        void 폴더_주인이_떠나려고_시도할_경우_400응답을_반환한다() throws Exception {
            // given
            var folderOwner = accountCommand
                .create(Account.builder()
                    .name(new AccountName("폴더 주인")).email("example1@email.com").profile("url")
                    .providerKey(11111).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(new AccountId(1L))
                    .build()
                );

            var accountCredentials = new AccountCredentials(folderOwner.getId(), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            // when, then
            mockMvc.perform(delete("/api/folders/{folderId}/members/me", folderAggregate.getId().value()))
                .andExpect(status().isBadRequest());
        }

        @Test
        void 폴더_공유_멤버가_아닌_회원이_요청할_경우_403응답을_반환한다() throws Exception {
            // given
            var folderOwner = accountCommand
                .create(Account.builder()
                    .name(new AccountName("폴더 주인")).email("example1@email.com").profile("url")
                    .providerKey(11111).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var anotherAccount = accountCommand
                .create(Account.builder()
                    .name(new AccountName("회원2")).email("example2@email.com").profile("url")
                    .providerKey(22222).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(new AccountId(1L))
                    .build()
                );

            var accountCredentials = new AccountCredentials(anotherAccount.getId(), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            // when, then
            mockMvc.perform(delete("/api/folders/{folderId}/members/me", folderAggregate.getId().value()))
                .andExpect(status().isForbidden());
        }
    }

    @Nested
    class 멤버_추방시키기_API {

        @Test
        void 멤버_추방_성공시_204응답을_반환한다() throws Exception {
            // given
            var folderOwner = accountCommand
                .create(Account.builder()
                    .name(new AccountName("폴더 주인")).email("example1@email.com").profile("url")
                    .providerKey(11111).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var anotherAccount = accountCommand
                .create(Account.builder()
                    .name(new AccountName("회원2")).email("example2@email.com").profile("url")
                    .providerKey(22222).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            var sharedMember = sharedMemberCommand
                .create(new SharedMemberCreate(folderAggregate.getId(), anotherAccount.getId()));

            var accountCredentials = new AccountCredentials(folderOwner.getId(), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            // when, then
            mockMvc.perform(delete(
                "/api/folders/{folderId}/members/{inviteeId}",
                    folderAggregate.getId().value(),
                    anotherAccount.getId().value()))
                .andExpect(status().isNoContent());
        }

        @Test
        void 로그인_하지_않은_사용자가_요청시_401응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenThrow(AuthenticationException.class);

            // when, then
            mockMvc.perform(delete(
                    "/api/folders/{folderId}/members/{inviteeId}",
                    1L,
                    2L))
                .andExpect(status().isUnauthorized());
        }

        @Test
        void 폴더의_주인이_아닌_회원이_요청시_403응답을_반환한다() throws Exception {
            // given
            var folderOwner = accountCommand
                .create(Account.builder()
                    .name(new AccountName("폴더 주인")).email("example1@email.com").profile("url")
                    .providerKey(11111).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var anotherAccount = accountCommand
                .create(Account.builder()
                    .name(new AccountName("회원2")).email("example2@email.com").profile("url")
                    .providerKey(22222).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var theOtherAccount = accountCommand
                .create(Account.builder()
                    .name(new AccountName("회원3")).email("example3@email.com").profile("url")
                    .providerKey(33333).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            var sharedMember = sharedMemberCommand
                .create(new SharedMemberCreate(folderAggregate.getId(), anotherAccount.getId()));

            var accountCredentials = new AccountCredentials(theOtherAccount.getId(), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            // when, then
            mockMvc.perform(delete(
                    "/api/folders/{folderId}/members/{inviteeId}",
                    folderAggregate.getId().value(),
                    anotherAccount.getId().value()))
                .andExpect(status().isForbidden());
        }

        @Test
        void 폴더_주인_스스로를_추방할_경우_400응답을_반환한다() throws Exception {
            // given
            var folderOwner = accountCommand
                .create(Account.builder()
                    .name(new AccountName("폴더 주인")).email("example1@email.com").profile("url")
                    .providerKey(11111).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var anotherAccount = accountCommand
                .create(Account.builder()
                    .name(new AccountName("회원2")).email("example2@email.com").profile("url")
                    .providerKey(22222).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            var sharedMember = sharedMemberCommand
                .create(new SharedMemberCreate(folderAggregate.getId(), anotherAccount.getId()));

            var accountCredentials = new AccountCredentials(folderOwner.getId(), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            // when, then
            mockMvc.perform(delete(
                    "/api/folders/{folderId}/members/{inviteeId}",
                    folderAggregate.getId().value(),
                    folderOwner.getId().value()))
                .andExpect(status().isBadRequest());
        }
    }
}