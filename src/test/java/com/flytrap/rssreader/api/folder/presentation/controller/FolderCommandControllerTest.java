package com.flytrap.rssreader.api.folder.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import com.flytrap.rssreader.api.folder.presentation.dto.FolderUpdateRequest;
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
class FolderCommandControllerTest {

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

    @Nested
    class 새_폴더_생성_API {

        @Test
        void 새_폴더_생성에_성공하면_201응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            var request = new FolderUpdateRequest("새 폴더");

            // when, then
            mockMvc.perform(post("/api/folders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.folderId").value("1"))
                .andExpect(jsonPath("$.data.folderName").value("새 폴더"));
        }

        @Test
        void 로그인_하지_않은_사용자가_요청시_401응답을_반환한다() throws Exception {
            // given
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenThrow(AuthenticationException.class);

            var request = new FolderUpdateRequest("새 폴더");

            // when, then
            mockMvc.perform(post("/api/folders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").hasJsonPath())
                .andExpect(jsonPath("$.message").hasJsonPath());
        }

        @Test
        void 새_폴더_생성시_폴더명이_255자_보다_크면_400응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            var request = new FolderUpdateRequest("A".repeat(256));

            // when, then
            mockMvc.perform(post("/api/folders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode")
                    .value("Input_FieldError_name"))
                .andExpect(jsonPath("$.message")
                    .value("폴더명은 1자 이상 255자 이하여야 합니다."));
        }

        @Test
        void 새_폴더_생성시_폴더명이_한_글자_미만이면_400응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            var request = new FolderUpdateRequest("");

            // when, then
            mockMvc.perform(post("/api/folders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode")
                    .value("Input_FieldError_name"))
                .andExpect(jsonPath("$.message")
                    .value("폴더명은 1자 이상 255자 이하여야 합니다."));
        }

    }

    @Nested
    class 폴더_제목_수정_API {

        @Test
        void 폴더_제목_수정_성공시_200응답을_반환한다() throws Exception {
            // given
            var account = accountCommand
                .create(Account.builder()
                    .name(new AccountName("회원1")).email("example@email.com").profile("url")
                    .providerKey(11111).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(account.getId())
                    .build()
                );

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            var request = new FolderUpdateRequest("수정할 폴더명");

            // when, then
            mockMvc.perform(patch("/api/folders/{folderId}", 1)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.folderId").value("1"))
                .andExpect(jsonPath("$.data.folderName").value("수정할 폴더명"));
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
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(folderOwner.getId())
                    .build()
                );

            var accountCredentials = new AccountCredentials(anotherAccount.getId(), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            var request = new FolderUpdateRequest("수정할 폴더명");

            // when, then
            mockMvc.perform(patch("/api/folders/{folderId}", 1)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").hasJsonPath())
                .andExpect(jsonPath("$.message").hasJsonPath());
        }

        @Test
        void 폴더_수정시_폴더명이_255자_보다_크면_400응답을_반환한다() throws Exception {
            // given
            var account = accountCommand
                .create(Account.builder()
                    .name(new AccountName("회원1")).email("example@email.com").profile("url")
                    .providerKey(11111).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(account.getId())
                    .build()
                );

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            var request = new FolderUpdateRequest("A".repeat(256));

            // when, then
            mockMvc.perform(patch("/api/folders/{folderId}", 1)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode")
                    .value("Input_FieldError_name"))
                .andExpect(jsonPath("$.message")
                    .value("폴더명은 1자 이상 255자 이하여야 합니다."));
        }

        @Test
        void 폴더_수정시_폴더명이_한_글자_미만이면_400응답을_반환한다() throws Exception {
            // given
            var account = accountCommand
                .create(Account.builder()
                    .name(new AccountName("회원1")).email("example@email.com").profile("url")
                    .providerKey(11111).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(account.getId())
                    .build()
                );

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            var request = new FolderUpdateRequest("");

            // when, then
            mockMvc.perform(patch("/api/folders/{folderId}", 1)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode")
                    .value("Input_FieldError_name"))
                .andExpect(jsonPath("$.message")
                    .value("폴더명은 1자 이상 255자 이하여야 합니다."));
        }
    }

    @Nested
    class 폴더_삭제_API {

        @Test
        void 폴더_삭제_성공시_204응답을_반환한다() throws Exception {
            // given
            var account = accountCommand
                .create(Account.builder()
                    .name(new AccountName("회원1")).email("example@email.com").profile("url")
                    .providerKey(11111).authProvider(AuthProvider.GITHUB).roll(AccountRoll.GENERAL)
                    .createdAt(Instant.now())
                    .build()
                );
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(account.getId())
                    .build()
                );

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            // when, then
            mockMvc.perform(delete("/api/folders/{folderId}", 1))
                .andExpect(status().isNoContent());
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
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(folderOwner.getId())
                    .build()
                );

            var accountCredentials = new AccountCredentials(anotherAccount.getId(), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            // when, then
            mockMvc.perform(delete("/api/folders/{folderId}", 1))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").hasJsonPath())
                .andExpect(jsonPath("$.message").hasJsonPath());
        }

    }

}