package com.flytrap.rssreader.api.alert.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.flytrap.rssreader.CustomServiceTest;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.alert.domain.AlertCreate;
import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.alert.infrastructure.implement.AlertCommand;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderCommand;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

@CustomServiceTest
class AlertQueryServiceTest {

    @Autowired
    AlertQueryService alertQueryService;

    @Autowired
    AlertCommand alertCommand;

    @Autowired
    FolderCommand folderCommand;

    @Nested
    class GetAlertsByFolder {

        @Test
        void 알림_목록을_불러올_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var myFolder = folderCommand.create(FolderCreate.builder()
                .name("폴더명")
                .ownerId(accountId)
                .build());
            var myAlert1 = alertCommand.create(new AlertCreate(
                myFolder.getId(), accountId,
                AlertPlatform.DISCORD, "https://discord.com/api/webhooks/webhookUrl01"));
            var myAlert2 = alertCommand.create(new AlertCreate(
                myFolder.getId(), accountId,
                AlertPlatform.DISCORD, "https://discord.com/api/webhooks/webhookUrl02"));
            var myAlert3 = alertCommand.create(new AlertCreate(
                myFolder.getId(), accountId,
                AlertPlatform.DISCORD, "https://discord.com/api/webhooks/webhookUrl03"));

            // when
            var alerts = alertQueryService.getAlertsByFolder(myFolder.getId(), accountId);

            // then
            assertThat(alerts).hasSize(3);
        }

        @Test
        void 폴더에_접근_권한이_없으면_예외를_던진다() {
            // given
            var accountId = new AccountId(1L);
            var folder = folderCommand.create(FolderCreate.builder()
                .name("폴더명")
                .ownerId(new AccountId(999L))
                .build());

            // when
            Executable alertsExecutable = () -> alertQueryService
                .getAlertsByFolder(folder.getId(), accountId);

            // then
            assertThrows(ForbiddenAccessFolderException.class, alertsExecutable);
        }
    }
}