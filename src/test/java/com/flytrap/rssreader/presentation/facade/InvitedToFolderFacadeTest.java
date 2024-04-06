package com.flytrap.rssreader.presentation.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.api.folder.business.facade.InvitedToFolderFacade;
import com.flytrap.rssreader.api.folder.business.service.SharedFolderReadService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.SharedFolder;
import com.flytrap.rssreader.api.member.business.service.MemberService;
import com.flytrap.rssreader.fixture.FixtureFactory;
import com.flytrap.rssreader.fixture.FolderFixtureFactory;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InvitedToFolderFacadeTest {

    @Mock
    SharedFolderReadService sharedFolderReadService;
    @Mock
    MemberService memberService;
    @InjectMocks
    InvitedToFolderFacade invitedToFolderFacade;

    @Nested
    @DisplayName("addInvitedMembersInFolder 메소드는")
    class AddInvitedMembersInFolder {

        @Test
        @DisplayName("폴더에 초대된 멤버를 추가한다.")
        void addInvitedMembersInFolder() {
            // given
            when(sharedFolderReadService.findMembersInFolders(any())).thenReturn(
                    Map.of(1L, List.of(1L, 2L)));
            when(memberService.findAllByIds(any())).thenReturn(
                    List.of(FixtureFactory.generateMember(),
                            FixtureFactory.generateAnotherMember()));

            // when
            List<? extends Folder> folders = invitedToFolderFacade.addInvitedMembersInFolder(
                    List.of(FolderFixtureFactory.generateSharedFolder()));

            if (folders.get(0) instanceof SharedFolder sharedFolder) {
                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    assertEquals(1, folders.size());
                    assertEquals(2, sharedFolder.getInvitedMembers().size());
                });
            } else {
                fail();
            }
        }

        @Test
        @DisplayName("폴더가 비공개일 경우 추가하지 않는다.")
        void addInvitedMembersInFolder_Private() {
            // given
            when(sharedFolderReadService.findMembersInFolders(any())).thenReturn(
                    Map.of(1L, List.of()));
            when(memberService.findAllByIds(any())).thenReturn(List.of());

            // when
            List<? extends Folder> folders = invitedToFolderFacade.addInvitedMembersInFolder(
                    List.of(FolderFixtureFactory.generateFolder()));

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                assertEquals(1, folders.size());
                assertEquals(false, folders.get(0).isShared());
            });
        }
    }
}
