package com.flytrap.rssreader.service.folder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.api.folder.business.service.FolderVerifyService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.api.folder.infrastructure.repository.SharedFolderJpaRepository;
import com.flytrap.rssreader.api.member.domain.Member;
import com.flytrap.rssreader.fixture.FixtureFactory;
import com.flytrap.rssreader.fixture.FolderFixtureFactory;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import com.flytrap.rssreader.global.exception.domain.NotBelongToMemberException;

import java.util.Optional;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Folder 생성 멤버 검증 로직 단위 테스트")
@ExtendWith(MockitoExtension.class)
class FolderVerifyServiceTest {

    @Mock
    FolderEntityJpaRepository repository;
    @Mock
    SharedFolderJpaRepository sharedFolderJpaRepository;

    @InjectMocks
    FolderVerifyService folderVerifyService;

    Member member;
    Member invitedMember;
    Member anotherMember;

    @Nested
    @DisplayName("Folder의 생성 멤버를 검증할 때")
    class getVerifiedFolder {

        @BeforeEach
        void init() {
            when(repository.findByIdAndIsDeletedFalse(1L)).thenReturn(
                    Optional.ofNullable(FolderFixtureFactory.generateFolderEntity()));

            member = FixtureFactory.generateMember();
            anotherMember = FixtureFactory.generateAnotherMember();
        }

        @Test
        @DisplayName("생성 멤버가 일치하면 Folder를 반환한다.")
        void is() {
            Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(1L, member.getId());

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(verifiedFolder.getId()).isEqualTo(1L);
                softAssertions.assertThat(verifiedFolder.getName()).isEqualTo("folderName");
                softAssertions.assertThat(verifiedFolder.getMemberId()).isEqualTo(member.getId());
            });
        }

        @Test
        @DisplayName("생성 멤버가 일치하지 않으면 예외를 던진다.")
        void isNot() {
            assertThrows(NotBelongToMemberException.class, () -> {
                folderVerifyService.getVerifiedOwnedFolder(1L, anotherMember.getId());
            });
        }
    }

    @Nested
    @DisplayName("Folder의 접근 멤버를 검증할 때")
    class getVerifiedAccessableFolder {

        @BeforeEach
        void init() {
            member = FixtureFactory.generateMember();
            invitedMember = FixtureFactory.generateMember(2L);
            anotherMember = FixtureFactory.generateAnotherMember();

            when(repository.findByIdAndIsDeletedFalse(1L)).thenReturn(
                    Optional.ofNullable(FolderFixtureFactory.generateFolderEntity()));
        }

        @Test
        @DisplayName("생성 멤버가 일치하면 Folder를 반환한다.")
        void isOwner() {
            Folder verifiedFolder = folderVerifyService.getVerifiedAccessableFolder(1L, member.getId());

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(verifiedFolder.getId()).isEqualTo(1L);
                softAssertions.assertThat(verifiedFolder.getName()).isEqualTo("folderName");
                softAssertions.assertThat(verifiedFolder.getMemberId()).isEqualTo(member.getId());
            });
        }

        @Test
        @DisplayName("초대된 멤버라면 Folder를 반환한다.")
        void isInvitedMember() {
            when(sharedFolderJpaRepository.existsByFolderIdAndMemberId(1L, invitedMember.getId())).thenReturn(true);

            Folder verifiedFolder = folderVerifyService.getVerifiedAccessableFolder(1L, invitedMember.getId());

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(verifiedFolder.getId()).isEqualTo(1L);
                softAssertions.assertThat(verifiedFolder.getName()).isEqualTo("folderName");
            });
        }

        @Test
        @DisplayName("생성 멤버도 아니고 초대된 멤버도 아니라면 예외를 던진다.")
        void isNot() {
            when(sharedFolderJpaRepository.existsByFolderIdAndMemberId(1L, anotherMember.getId())).thenReturn(false);

            assertThrows(ForbiddenAccessFolderException.class, () -> {
                folderVerifyService.getVerifiedAccessableFolder(1L, anotherMember.getId());
            });
        }
    }
}
