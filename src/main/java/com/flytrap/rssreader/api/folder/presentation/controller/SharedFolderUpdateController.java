package com.flytrap.rssreader.api.folder.presentation.controller;

import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.folder.business.service.FolderUpdateService;
import com.flytrap.rssreader.api.folder.business.service.FolderVerifyService;
import com.flytrap.rssreader.api.folder.business.service.SharedFolderReadService;
import com.flytrap.rssreader.api.folder.business.service.SharedFolderUpdateService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.presentation.controller.swagger.SharedFolderUpdateControllerApi;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import javax.security.sasl.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class SharedFolderUpdateController implements SharedFolderUpdateControllerApi {

    private final SharedFolderReadService sharedFolderReadService;
    private final SharedFolderUpdateService sharedFolderUpdateService;
    private final FolderUpdateService folderUpdateService;
    private final FolderVerifyService folderVerifyService;

    // 공유 폴더에 사람 나가기 (내가 스스로 나간다)
    @DeleteMapping("/{folderId}/members/me-delete")
    public ApplicationResponse<String> leaveFolder(
            @PathVariable Long folderId,
            @Login AccountCredentials member
    ) {
        Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, member.id().value());
        
        sharedFolderUpdateService.leave(verifiedFolder, member.id().value());

        if (sharedFolderReadService.countAllMembersByFolder(folderId) <= 0) {
            folderUpdateService.makePrivate(verifiedFolder);
        }

        return ApplicationResponse.success();
    }

    //공유 폴더에 사람 삭제하기 (만든 사람만)
    @DeleteMapping("/{folderId}/members/{inviteeId}-delete")
    public ApplicationResponse<String> deleteMember(
            @PathVariable Long folderId,
            @PathVariable Long inviteeId,
            @Login AccountCredentials member
    ) throws AuthenticationException {
        Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, member.id().value());

        sharedFolderUpdateService.removeFolderMember(verifiedFolder, inviteeId, member.id().value());

        if (sharedFolderReadService.countAllMembersByFolder(folderId) <= 0) {
            folderUpdateService.makePrivate(verifiedFolder);
        }

        return ApplicationResponse.success();
    }
}
