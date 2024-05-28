package com.flytrap.rssreader.api.shared_member.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.flytrap.rssreader.CustomServiceTest;
import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.account.domain.AccountName;
import com.flytrap.rssreader.api.account.domain.AccountRoll;
import com.flytrap.rssreader.api.account.domain.AuthProvider;
import com.flytrap.rssreader.api.account.infrastructure.implement.AccountCommand;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.domain.SharedStatus;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderCommand;
import com.flytrap.rssreader.api.shared_member.domain.SharedMember;
import com.flytrap.rssreader.api.shared_member.domain.SharedMemberCreate;
import com.flytrap.rssreader.api.shared_member.infrastructure.implementation.SharedMemberCommand;
import com.flytrap.rssreader.api.shared_member.infrastructure.implementation.SharedMemberQuery;
import com.flytrap.rssreader.api.shared_member.infrastructure.implementation.SharedMemberValidator;
import com.flytrap.rssreader.global.exception.domain.DuplicateDomainException;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import com.flytrap.rssreader.global.exception.domain.NotFolderOwnerException;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

@CustomServiceTest
class SharedMemberCommandServiceTest {

    @Autowired
    SharedMemberCommandService sharedMemberCommandService;

    @Autowired
    SharedMemberQuery sharedMemberQuery;

    @Autowired
    SharedMemberCommand sharedMemberCommand;

    @Autowired
    FolderCommand folderCommand;

    @Autowired
    AccountCommand accountCommand;

    @Autowired
    SharedMemberValidator sharedMemberValidator;

    @Nested
    class InviteMemberToFolder {

        @Test
        void 폴더_주인은_폴더에_멤버를_초대할_수_있다() {
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

            // when
            SharedMember newSharedMember = sharedMemberCommandService
                .inviteMemberToFolder(
                    folderAggregate.getId(),
                    folderOwner.getId(),
                    anotherAccount.getId()
                );

            // then
            assertAll(
                () -> assertThat(newSharedMember.getId()).isNotNull(),
                () -> assertThat(newSharedMember)
                    .hasFieldOrPropertyWithValue("accountId", anotherAccount.getId())
                    .hasFieldOrPropertyWithValue("name", anotherAccount.getName().value())
                    .hasFieldOrPropertyWithValue("profileUrl", anotherAccount.getProfile())
            );
        }

        @Test
        void 폴더의_주인이_아니면_멤버를_초대할_수_없다() {
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

            // when
            Executable inviteMemberExecutable = () -> sharedMemberCommandService
                .inviteMemberToFolder(
                    folderAggregate.getId(),
                    anotherAccount.getId(),
                    folderOwner.getId()
                );

            // then
            assertThrows(NotFolderOwnerException.class, inviteMemberExecutable);
        }

        @Test
        void 자기_자신을_초대할_수_없다() {
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

            // when
            Executable inviteMemberExecutable = () -> sharedMemberCommandService
                .inviteMemberToFolder(
                    folderAggregate.getId(),
                    folderOwner.getId(),
                    folderOwner.getId()
                );

            // then
            assertThrows(IllegalArgumentException.class, inviteMemberExecutable);
        }

        @Test
        void 이미_초대된_멤버는_초대할_수_없다() {
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

            // when
            Executable inviteMemberExecutable = () -> sharedMemberCommandService
                .inviteMemberToFolder(
                    folderAggregate.getId(),
                    folderOwner.getId(),
                    anotherAccount.getId()
                );

            // then
            assertThrows(DuplicateDomainException.class, inviteMemberExecutable);
        }

        @Test
        void 폴더에_멤버가_초대되면_공유_폴더로_전환된다() {
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

            // when
            SharedMember newSharedMember = sharedMemberCommandService
                .inviteMemberToFolder(
                    folderAggregate.getId(),
                    folderOwner.getId(),
                    anotherAccount.getId()
                );
            var sharedFolder = folderCommand
                .readAggregate(folderAggregate.getId()).get();

            // then
            assertThat(sharedFolder.getSharedStatus())
                .isEqualTo(SharedStatus.SHARED);
        }

    }

    @Nested
    class LeaveFolder {

        @Test
        void 공유_멤버는_폴더에서_떠날_수_있다() {
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

            // when
            sharedMemberCommandService.leaveFolder(folderAggregate.getId(), anotherAccount.getId());
            Optional<SharedMember> sharedMemberResult = sharedMemberQuery
                .readAllByFolder(folderAggregate.getId()).stream()
                .filter(member -> member.getId().equals(sharedMember.getId()))
                .findFirst();

            // then
            assertThat(sharedMemberResult).isNotPresent();
        }

        @Test
        void 폴더의_주인은_떠날_수_없다() {
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

            // when
            Executable sharedMemberExecutable = () ->
                sharedMemberCommandService.leaveFolder(folderAggregate.getId(), folderOwner.getId());

            // then
            assertThrows(IllegalArgumentException.class, sharedMemberExecutable);
        }

        @Test
        void 폴더에_접근할_수_없는_멤버는_떠날_수_없다() {
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

            // when
            Executable sharedMemberExecutable = () -> sharedMemberCommandService
                .leaveFolder(folderAggregate.getId(), theOtherAccount.getId());

            // then
            assertThrows(ForbiddenAccessFolderException.class, sharedMemberExecutable);
        }

        @Test
        void 폴더에_공유_멤버가_없을_경우_개인_폴더로_전환된다() {
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

            // when
            sharedMemberCommandService.leaveFolder(folderAggregate.getId(), anotherAccount.getId());
            var sharedFolder = folderCommand
                .readAggregate(folderAggregate.getId()).get();

            // then
            assertThat(sharedFolder.getSharedStatus())
                .isEqualTo(SharedStatus.PRIVATE);
        }
    }

    @Nested
    class RemoveMemberFromFolder {

        @Test
        void 폴더_주인은_폴더의_공유_멤버를_추방시킬_수_있다() {
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

            // when
            sharedMemberCommandService
                .removeMemberFromFolder(
                    folderAggregate.getId(), folderOwner.getId(), anotherAccount.getId()
                );
            Optional<SharedMember> sharedMemberResult = sharedMemberQuery
                .readAllByFolder(folderAggregate.getId()).stream()
                .filter(member -> member.getId().equals(sharedMember.getId()))
                .findFirst();

            // then
            assertThat(sharedMemberResult).isNotPresent();
        }

        @Test
        void 폴더의_주인이_아니면_멤버를_추방시킬_수_없다() {
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

            // when
            Executable sharedMemberExecutable = () -> sharedMemberCommandService
                .removeMemberFromFolder(
                    folderAggregate.getId(),
                    theOtherAccount.getId(),
                    anotherAccount.getId()
                );

            // then
            assertThrows(NotFolderOwnerException.class, sharedMemberExecutable);
        }

        @Test
        void 자기_자신을_추방시킬_수_없다() {
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

            // when
            Executable sharedMemberExecutable = () -> sharedMemberCommandService
                .removeMemberFromFolder(
                    folderAggregate.getId(),
                    folderOwner.getId(),
                    folderOwner.getId()
                );

            // then
            assertThrows(IllegalArgumentException.class, sharedMemberExecutable);
        }

        @Test
        void 폴더에_공유_멤버가_없을_경우_개인_폴더로_전환된다() {
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

            // when
            sharedMemberCommandService.removeMemberFromFolder(
                folderAggregate.getId(), folderOwner.getId(), anotherAccount.getId());
            var sharedFolder = folderCommand
                .readAggregate(folderAggregate.getId()).get();

            // then
            assertThat(sharedFolder.getSharedStatus())
                .isEqualTo(SharedStatus.PRIVATE);
        }

    }
}