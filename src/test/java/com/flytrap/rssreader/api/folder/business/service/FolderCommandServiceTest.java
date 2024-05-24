package com.flytrap.rssreader.api.folder.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.flytrap.rssreader.CustomTestConfig;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.domain.SharedStatus;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderCommand;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

@CustomTestConfig
class FolderCommandServiceTest {

    @Autowired
    FolderCommandService folderCommandService;
    @Autowired
    FolderCommand folderCommand;

    @Nested
    class CreateNewFolder {

        @Test
        void 새_폴더를_생성할_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var folderName = "폴더 이름";

            // when
            var newFolder
                = folderCommandService.createNewFolder(accountId, folderName);

            // then
            assertAll(
                () -> assertThat(newFolder.getId()).isNotNull(),
                () -> assertThat(newFolder)
                    .hasFieldOrPropertyWithValue("name", folderName)
                    .hasFieldOrPropertyWithValue("ownerId", accountId)
                    .hasFieldOrPropertyWithValue("sharedStatus", SharedStatus.PRIVATE)
            );
        }
    }

    @Nested
    class UpdateFolder {

        @Test
        void 폴더_주인은_폴더명을_변경할_수_있다() {
            // given
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(new AccountId(1L))
                    .build()
                );

            var ownerId = folderAggregate.getOwnerId();
            var folderId = folderAggregate.getId();
            var folderName = "수정할 폴더 이름";

            // when
            var updatedFolder = folderCommandService
                .updateFolder(ownerId, folderId, folderName);

            // then
            assertAll(
                () -> assertThat(updatedFolder)
                    .hasFieldOrPropertyWithValue("id", folderId)
                    .hasFieldOrPropertyWithValue("ownerId", ownerId)
                    .hasFieldOrPropertyWithValue("name", folderName)
            );
        }

        @Test
        void 폴더_주인이_아니면_폴더명을_변경할_수_없다() {
            // given
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(new AccountId(1L))
                    .build()
                );

            var folderId = folderAggregate.getId();
            var folderName = "수정할 폴더 이름";
            var anotherAccountId = new AccountId(99L);

            // when
            Executable updateFolderExecutable = ()
                -> folderCommandService.updateFolder(anotherAccountId, folderId, folderName);

            // then
            assertThrows(ForbiddenAccessFolderException.class, updateFolderExecutable);
        }

        @Test
        void 존재하지_않는_폴더는_변경할_수_없다() {
            // given
            var accountId = new AccountId(1L);
            var folderId = new FolderId(99L);
            var folderName = "수정할 폴더 이름";

            // when
            Executable updateFolderExecutable = ()
                -> folderCommandService.updateFolder(accountId, folderId, folderName);

            // then
            assertThrows(ForbiddenAccessFolderException.class, updateFolderExecutable);
        }
    }

    @Nested
    class DeleteFolder {

        @Test
        void 폴더_주인은_폴더를_삭제할_수_있다() {
            // given
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(new AccountId(1L))
                    .build()
                );

            var ownerId = folderAggregate.getOwnerId();
            var folderId = folderAggregate.getId();

            // when
            folderCommandService.deleteFolder(ownerId, folderId);

            // then
            assertThat(folderCommand.readAggregate(folderId).isEmpty())
                .isTrue();
        }

        @Test
        void 폴더_주인이_아니면_폴더를_삭제할_수_없다() {
            // given
            var folderAggregate = folderCommand
                .create(FolderCreate.builder()
                    .name("폴더 이름")
                    .ownerId(new AccountId(1L))
                    .build()
                );

            var folderId = folderAggregate.getId();
            var anotherAccountId = new AccountId(99L);

            // when
            Executable deleteFolderExecutable = ()
                -> folderCommandService.deleteFolder(anotherAccountId, folderId);

            // then
            assertThrows(ForbiddenAccessFolderException.class, deleteFolderExecutable);
        }

        @Test
        void 존재하지_않는_폴더는_삭제할_수_없다() {
            // given
            var accountId = new AccountId(1L);
            var folderId = new FolderId(99L);

            // when
            Executable deleteFolderExecutable = ()
                -> folderCommandService.deleteFolder(accountId, folderId);

            // then
            assertThrows(ForbiddenAccessFolderException.class, deleteFolderExecutable);
        }
    }

}