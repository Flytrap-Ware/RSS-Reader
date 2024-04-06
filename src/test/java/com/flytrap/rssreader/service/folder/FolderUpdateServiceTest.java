package com.flytrap.rssreader.service.folder;

import static org.mockito.Mockito.when;

import com.flytrap.rssreader.api.folder.business.service.FolderUpdateService;
import com.flytrap.rssreader.api.folder.business.service.FolderVerifyService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.api.folder.presentation.dto.FolderRequest;
import com.flytrap.rssreader.api.member.domain.Member;
import com.flytrap.rssreader.fixture.FixtureFactory;
import com.flytrap.rssreader.fixture.FolderFixtureFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Folder 서비스 로직 단위 테스트")
@ExtendWith(MockitoExtension.class)
class FolderUpdateServiceTest {

    @Mock
    FolderVerifyService folderVerifyService;

    @Mock
    FolderEntityJpaRepository repository;

    @InjectMocks
    FolderUpdateService folderService;

    Member member = FixtureFactory.generateMember();

    Folder folder = FolderFixtureFactory.generateFolder();

    String newFolderName = "newFolderName";

    @Test
    @DisplayName("folder 생성에 성공한다.")
    void createFolder_success() {
        when(repository.save(Mockito.any(FolderEntity.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        // given
        FolderRequest.CreateRequest request = new FolderRequest.CreateRequest(newFolderName);

        // when
        Folder newFolder = folderService.createNewFolder(request, member.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(newFolder.getName()).isEqualTo(newFolderName);
            softAssertions.assertThat(newFolder.getMemberId()).isEqualTo(member.getId());
        });
    }

    @Nested
    @DisplayName("folder 이름 수정 시")
    class ModifyFolderName {

        @Test
        @DisplayName("folder 이름이 성공적으로 변경된다.")
        void modifyFolderName_success() {
            when(repository.save(Mockito.any(FolderEntity.class)))
                    .thenAnswer(i -> i.getArguments()[0]);

            // given
            FolderRequest.CreateRequest request = new FolderRequest.CreateRequest(newFolderName);

            // when
            Folder updatedFolder = folderService.updateFolder(request, folder,
                    member.getId());

            // then
            // exception 없이 정상적으로 종료
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(updatedFolder.getName()).isEqualTo(newFolderName);
                softAssertions.assertThat(updatedFolder.getId()).isEqualTo(folder.getId());
                softAssertions.assertThat(updatedFolder.getMemberId()).isEqualTo(member.getId());
            });
        }

        //TODO Controller 테스트에서 테스트
//        @Test
//        @DisplayName("작성자가 아닐 시 이름이 수정되지 않는다.")
//        void modifyFolderName_fail() {
//            // given
//            FolderRequest.CreateRequest request = new FolderRequest.CreateRequest(newFolderName);
//            when(folderVerifyOwnerService.getVerifiedFolder(any(Long.class), any(Long.class)))
//                    .thenThrow(new NotBelongToMemberException(folder));
//
//            SoftAssertions.assertSoftly(softAssertions -> {
//                softAssertions.assertThatThrownBy(
//                                () -> folderService.updateFolder(request, folder, 2L))
//                        .isInstanceOf(NotBelongToMemberException.class);
//                softAssertions.assertThat(folder.getName())
//                        .isEqualTo(FolderFixtureFactory.FolderFields.name);
//            });
//        }
    }

    @Nested
    @DisplayName("folder 삭제 시")
    class DeleteFolder {

        @Test
        @DisplayName("folder가 삭제된다.")
        void deleteFolder_success() {
            when(repository.save(Mockito.any(FolderEntity.class)))
                    .thenAnswer(i -> i.getArguments()[0]);

            // when
            Folder deletedFolder = folderService.deleteFolder(folder, member.getId());
            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(deletedFolder.isDeleted()).isTrue();
                softAssertions.assertThat(deletedFolder.getId()).isEqualTo(folder.getId());
                softAssertions.assertThat(deletedFolder.getMemberId()).isEqualTo(member.getId());
            });
        }

        //TODO Controller 테스트에서 테스트
//        @Test
//        @DisplayName("작성자가 아닐 시 삭제되지 않는다.")
//        void deleteFolder_authException() {
//            when(folderVerifyOwnerService.getVerifiedFolder(any(Long.class), any(Long.class)))
//                    .thenThrow(new NotBelongToMemberException(folder));
//
//            // when
//            SoftAssertions.assertSoftly(softAssertions -> {
//                softAssertions.assertThatThrownBy(() -> {
//                    folderService.deleteFolder(folder, member.getId());
//                }).isInstanceOf(NotBelongToMemberException.class);
//
//                softAssertions.assertThat(folder.isDeleted()).isFalse();
//                verify(repository, Mockito.times(0)).save(Mockito.any(FolderEntity.class));
//            });
//        }
    }

    @Test
    @DisplayName("folder 공유 상태로 바꾼다.")
    void shareFolder_success() {
        when(repository.save(Mockito.any(FolderEntity.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        // given
        FolderRequest.CreateRequest request = new FolderRequest.CreateRequest(newFolderName);

        // when
        Folder newFolder = folderService.createNewFolder(request, member.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(newFolder.getName()).isEqualTo(newFolderName);
            softAssertions.assertThat(newFolder.getMemberId()).isEqualTo(member.getId());
        });
    }

}
