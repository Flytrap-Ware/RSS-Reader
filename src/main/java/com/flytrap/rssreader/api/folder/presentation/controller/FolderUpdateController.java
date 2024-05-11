package com.flytrap.rssreader.api.folder.presentation.controller;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.folder.business.service.FolderSubscribeService;
import com.flytrap.rssreader.api.folder.business.service.FolderUpdateService;
import com.flytrap.rssreader.api.folder.business.service.FolderVerifyService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderAggregate;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.presentation.controller.swagger.FolderUpdateControllerApi;
import com.flytrap.rssreader.api.folder.presentation.dto.FolderUpdateRequest;
import com.flytrap.rssreader.api.folder.presentation.dto.FolderUpdateResponse;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class FolderUpdateController implements FolderUpdateControllerApi {

    private final FolderUpdateService folderUpdateService;
    private final FolderVerifyService folderVerifyService;
    private final FolderSubscribeService folderSubscribeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<FolderUpdateResponse> createNewFolder(
            @Valid @RequestBody FolderUpdateRequest request,
            @Login AccountCredentials accountCredentials) {

        FolderAggregate newFolder = folderUpdateService
            .createNewFolder(new AccountId(accountCredentials.id().value()), request.name());

        return new ApplicationResponse<>(FolderUpdateResponse.from(newFolder));
    }

    @PatchMapping("/{folderId}")
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<FolderUpdateResponse> updateFolder(
            @Valid @RequestBody FolderUpdateRequest request,
            @PathVariable Long folderId,
            @Login AccountCredentials accountCredentials) {

        FolderAggregate folder = folderUpdateService
            .updateFolder(new AccountId(accountCredentials.id().value()), new FolderId(folderId), request.name());

        return new ApplicationResponse<>(FolderUpdateResponse.from(folder));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{folderId}")
    public ApplicationResponse<Void> deleteFolder(
            @PathVariable Long folderId,
            @Login AccountCredentials accountCredentials) {

        folderUpdateService.deleteFolder(new AccountId(accountCredentials.id().value()), new FolderId(folderId));

        return new ApplicationResponse<>(null);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{folderId}/rss/{subscribeId}")
    public ApplicationResponse<Void> unsubscribe(
            @PathVariable Long folderId,
            @PathVariable Long subscribeId,
            @Login AccountCredentials member) {

        Folder verifiedFolder = folderVerifyService.getVerifiedAccessableFolder(folderId, member.id().value());
        folderSubscribeService.folderUnsubscribe(subscribeId,
                verifiedFolder.getId());
        return new ApplicationResponse<>(null);
    }
}
