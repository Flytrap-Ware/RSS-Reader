package com.flytrap.rssreader.api.folder.presentation.controller;

import com.flytrap.rssreader.api.folder.business.service.FolderUpdateService;
import com.flytrap.rssreader.api.folder.business.service.FolderVerifyService;
import com.flytrap.rssreader.api.folder.business.service.SharedFolderReadService;
import com.flytrap.rssreader.api.folder.business.service.SharedFolderUpdateService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.presentation.controller.swagger.SharedFolderUpdateControllerApi;
import com.flytrap.rssreader.api.member.business.service.MemberService;
import com.flytrap.rssreader.api.member.domain.Member;
import com.flytrap.rssreader.api.member.presentation.dto.response.MemberSummary;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.InviteMemberRequest;
import com.flytrap.rssreader.api.auth.presentation.dto.SessionMember;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class SharedFolderUpdateController implements SharedFolderUpdateControllerApi {

    private final SharedFolderReadService sharedFolderReadService;
    private final SharedFolderUpdateService sharedFolderUpdateService;
    private final FolderUpdateService folderUpdateService;
    private final FolderVerifyService folderVerifyService;
    private final MemberService memberService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{folderId}/members")
    public ApplicationResponse<MemberSummary> inviteMember(
            @PathVariable Long folderId,
            @Login SessionMember loginMember,
            @RequestBody InviteMemberRequest request
    ) throws AuthenticationException {

        Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, loginMember.id());
        Member member = memberService.findById(request.inviteeId());
        sharedFolderUpdateService.invite(verifiedFolder, member.getId());
        folderUpdateService.shareFolder(verifiedFolder);

        return new ApplicationResponse<>(MemberSummary.from(member));
    }

    // 공유 폴더에 사람 나가기 (내가 스스로 나간다)
    @DeleteMapping("/{folderId}/members/me")
    public ApplicationResponse<String> leaveFolder(
            @PathVariable Long folderId,
            @Login SessionMember member
    ) {
        Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, member.id());
        
        sharedFolderUpdateService.leave(verifiedFolder, member.id());

        if (sharedFolderReadService.countAllMembersByFolder(folderId) <= 0) {
            folderUpdateService.makePrivate(verifiedFolder);
        }

        return ApplicationResponse.success();
    }

    //공유 폴더에 사람 삭제하기 (만든 사람만)
    @DeleteMapping("/{folderId}/members/{inviteeId}")
    public ApplicationResponse<String> deleteMember(
            @PathVariable Long folderId,
            @PathVariable Long inviteeId,
            @Login SessionMember member
    ) throws AuthenticationException {
        Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, member.id());

        sharedFolderUpdateService.removeFolderMember(verifiedFolder, inviteeId, member.id());

        if (sharedFolderReadService.countAllMembersByFolder(folderId) <= 0) {
            folderUpdateService.makePrivate(verifiedFolder);
        }

        return ApplicationResponse.success();
    }
}
