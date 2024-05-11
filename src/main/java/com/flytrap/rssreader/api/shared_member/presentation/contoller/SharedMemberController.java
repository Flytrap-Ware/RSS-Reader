package com.flytrap.rssreader.api.shared_member.presentation.contoller;

import com.flytrap.rssreader.api.account.presentation.dto.AccountSummaryResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.shared_member.presentation.contoller.swagger.SharedMemberControllerApi;
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
public class SharedMemberController implements SharedMemberControllerApi {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/folders/{folderId}/members")
    public ApplicationResponse<AccountSummaryResponse> inviteMemberToFolder(
        @PathVariable Long folderId,
        @Valid @RequestBody InviteMemberRequest request,
        @Login AccountCredentials accountCredentials
    ) {
        return null;
    }

    @DeleteMapping("/api/folders/{folderId}/members/me")
    public ApplicationResponse<String> leaveFolder(
        @PathVariable Long folderId,
        @Login AccountCredentials accountCredentials
    ) {
        return null;
    }

    @DeleteMapping("/api/folders/{folderId}/members/{inviteeId}")
    public ApplicationResponse<String> removeMemberFromFolder(
        @PathVariable Long folderId, @PathVariable Long inviteeId,
        @Login AccountCredentials accountCredentials
    ) {
        return null;
    }
}
