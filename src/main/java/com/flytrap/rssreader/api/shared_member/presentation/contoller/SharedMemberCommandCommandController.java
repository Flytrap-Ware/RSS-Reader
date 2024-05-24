package com.flytrap.rssreader.api.shared_member.presentation.contoller;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.account.presentation.dto.AccountSummaryResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.shared_member.business.service.SharedMemberCommandService;
import com.flytrap.rssreader.api.shared_member.domain.SharedMember;
import com.flytrap.rssreader.api.shared_member.presentation.contoller.swagger.SharedMemberCommandControllerApi;
import com.flytrap.rssreader.api.shared_member.presentation.dto.InviteMemberRequest;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SharedMemberCommandCommandController implements SharedMemberCommandControllerApi {

    private final SharedMemberCommandService sharedMemberCommandService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/folders/{folderId}/members")
    public ApplicationResponse<AccountSummaryResponse> inviteMemberToFolder(
        @PathVariable Long folderId,
        @Valid @RequestBody InviteMemberRequest request,
        @Login AccountCredentials accountCredentials
    ) {
        SharedMember sharedMember = sharedMemberCommandService.inviteMemberToFolder(
            new FolderId(folderId),
            accountCredentials.id(),
            new AccountId(request.inviteeId())
        );

        return new ApplicationResponse<>(AccountSummaryResponse.from(sharedMember));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/folders/{folderId}/members/me")
    public ApplicationResponse<Void> leaveFolder(
        @PathVariable Long folderId,
        @Login AccountCredentials accountCredentials
    ) {
        sharedMemberCommandService.leaveFolder(
            new FolderId(folderId), accountCredentials.id()
        );

        return new ApplicationResponse<>(null);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/folders/{folderId}/members/{inviteeId}")
    public ApplicationResponse<Void> removeMemberFromFolder(
        @PathVariable Long folderId, @PathVariable Long inviteeId,
        @Login AccountCredentials accountCredentials
    ) {
        sharedMemberCommandService.removeMemberFromFolder(
            new FolderId(folderId),
            accountCredentials.id(),
            new AccountId(inviteeId)
        );

        return new ApplicationResponse<>(null);
    }
}
