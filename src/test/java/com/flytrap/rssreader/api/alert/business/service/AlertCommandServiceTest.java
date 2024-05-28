package com.flytrap.rssreader.api.alert.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.flytrap.rssreader.CustomServiceTest;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.alert.domain.AlertCreate;
import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.alert.infrastructure.implement.AlertCommand;
import com.flytrap.rssreader.api.alert.infrastructure.implement.AlertQuery;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderCommand;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

@CustomServiceTest
class AlertCommandServiceTest {

    @Autowired
    AlertCommandService alertCommandService;

    @Autowired
    AlertCommand alertCommand;

    @Autowired
    AlertQuery alertQuery;

    @Autowired
    FolderCommand folderCommand;

    @Nested
    class RegisterAlert {

        @Test
        void 폴더에_새로운_알림을_등록할_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var myFolder = folderCommand.create(FolderCreate.builder()
                .name("폴더명")
                .ownerId(accountId)
                .build());
            var webhookUrl = "https://discord.com/api/webhooks/webhookUrl";

            // when
            var newAlert = alertCommandService.registerAlert(
                myFolder.getId(),
                accountId,
                webhookUrl
            );

            // then
            assertAll(
                () -> assertThat(newAlert.getId()).isNotNull(),
                () -> assertThat(newAlert)
                    .hasFieldOrPropertyWithValue("accountId", accountId)
                    .hasFieldOrPropertyWithValue("folderId", myFolder.getId())
                    .hasFieldOrPropertyWithValue("alertPlatform", AlertPlatform.DISCORD)
                    .hasFieldOrPropertyWithValue("webhookUrl", webhookUrl)
            );
        }

        @Test
        void 접근_불가능한_폴더에_알림을_추가하면_예외를_던진다() {
            // given
            var accountId = new AccountId(1L);
            var myFolder = folderCommand.create(FolderCreate.builder()
                .name("폴더명")
                .ownerId(new AccountId(999L))
                .build());
            var webhookUrl = "https://discord.com/api/webhooks/webhookUrl";

            // when
            Executable alertExecutable = () -> alertCommandService.registerAlert(
                myFolder.getId(),
                accountId,
                webhookUrl
            );

            // then
            assertThrows(ForbiddenAccessFolderException.class, alertExecutable);
        }

        @Test
        void 잘못된_알림_URL을_등록할_경우_예외를_던진다() {
            // given
            var accountId = new AccountId(1L);
            var myFolder = folderCommand.create(FolderCreate.builder()
                .name("폴더명")
                .ownerId(accountId)
                .build());
            var webhookUrl = "wrong webhookUrl";

            // when
            Executable alertExecutable = () -> alertCommandService.registerAlert(
                myFolder.getId(),
                accountId,
                webhookUrl
            );

            // then
            assertThrows(IllegalArgumentException.class, alertExecutable);
        }
    }

    @Nested
    class RemoveAlert {

        @Test
        void 폴더에_등록된_알림을_제거할_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var myFolder = folderCommand.create(FolderCreate.builder()
                .name("폴더명")
                .ownerId(accountId)
                .build());
            var myAlert = alertCommand.create(new AlertCreate(
                myFolder.getId(), accountId,
                AlertPlatform.DISCORD, "https://discord.com/api/webhooks/webhookUrl"));

            // when
            alertCommandService.removeAlert(myFolder.getId(), accountId, myAlert.getId());
            var alerts = alertQuery.readAllByFolder(myFolder.getId());

            // then
            assertThat(alerts).hasSize(0);
        }

        @Test
        void 접근_불가능한_폴더에_알림을_제거하려_하면_예외를_던진다() {
            // given
            var accountId = new AccountId(1L);
            var folder = folderCommand.create(FolderCreate.builder()
                .name("폴더명")
                .ownerId(new AccountId(999L))
                .build());
            var alert = alertCommand.create(new AlertCreate(
                folder.getId(), accountId,
                AlertPlatform.DISCORD, "https://discord.com/api/webhooks/webhookUrl"));

            // when
            Executable alertExecutable = () -> alertCommandService.
                removeAlert(folder.getId(), accountId, alert.getId());

            // then
            assertThrows(ForbiddenAccessFolderException.class, alertExecutable);
        }

    }
}