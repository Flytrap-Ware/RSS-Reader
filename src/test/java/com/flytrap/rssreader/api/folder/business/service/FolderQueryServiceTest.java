package com.flytrap.rssreader.api.folder.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.flytrap.rssreader.CustomServiceTest;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderCommand;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@CustomServiceTest
class FolderQueryServiceTest {

    @Autowired
    FolderQueryService folderQueryService;
    @Autowired
    FolderCommand folderCommand;

    @Nested
    class GetMyFolders {

        @Test
        void 회원은_접근_가능한_폴더_목록을_불러올_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var privateFolder1 = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름 1")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            var privateFolder2 = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름 2")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            var privateFolder3 = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름 3")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            var sharedFolder1 = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름 4")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            sharedFolder1.toShared();
            folderCommand.update(sharedFolder1);
            var sharedFolder2 = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름 5")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            sharedFolder2.toShared();
            folderCommand.update(sharedFolder2);

            // when
            var accessibleFolders = folderQueryService.getMyFolders(accountId);

            // then
            assertAll(
                () -> assertThat(accessibleFolders.privateFolders()).hasSize(3),
                () -> assertThat(accessibleFolders.sharedFolder()).hasSize(2)
            );
        }

        @Test
        void 개인_폴더와_공유_폴더를_분리해서_불러온다() {
            // given
            var accountId = new AccountId(1L);
            var privateFolder1 = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름 1")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            var privateFolder2 = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름 2")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            var privateFolder3 = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름 3")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            var sharedFolder1 = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름 4")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            sharedFolder1.toShared();
            folderCommand.update(sharedFolder1);
            var sharedFolder2 = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름 5")
                    .ownerId(new AccountId(1L))
                    .build()
                );
            sharedFolder2.toShared();
            folderCommand.update(sharedFolder2);

            // when
            var accessibleFolders = folderQueryService.getMyFolders(accountId);
            var privateFolders = accessibleFolders.privateFolders();
            var sharedFolders = accessibleFolders.sharedFolder();

            // then
            assertAll(
                () -> assertThat(privateFolders.get(0).isPrivate()).isTrue(),
                () -> assertThat(privateFolders.get(1).isPrivate()).isTrue(),
                () -> assertThat(privateFolders.get(2).isPrivate()).isTrue(),
                () -> assertThat(sharedFolders.get(0).isShared()).isTrue(),
                () -> assertThat(sharedFolders.get(1).isShared()).isTrue()
            );
        }
    }


}