package com.flytrap.rssreader.api.folder.presentation.controller;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.folder.business.service.FolderCommandService;
import com.flytrap.rssreader.api.folder.domain.FolderAggregate;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.presentation.controller.swagger.FolderCommandControllerApi;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FolderCommandController implements FolderCommandControllerApi {

    private final FolderCommandService folderCommandService;

    @PostMapping("/api/folders")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<FolderUpdateResponse> createNewFolder(
            @Valid @RequestBody FolderUpdateRequest request,
            @Login AccountCredentials accountCredentials) {

        FolderAggregate newFolder = folderCommandService
            .createNewFolder(new AccountId(accountCredentials.id().value()), request.name());

        return new ApplicationResponse<>(FolderUpdateResponse.from(newFolder));
    }

    @PatchMapping("/api/folders/{folderId}")
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<FolderUpdateResponse> updateFolder(
            @Valid @RequestBody FolderUpdateRequest request,
            @PathVariable Long folderId,
            @Login AccountCredentials accountCredentials) {

        FolderAggregate folder = folderCommandService
            .updateFolder(new AccountId(accountCredentials.id().value()), new FolderId(folderId), request.name());

        return new ApplicationResponse<>(FolderUpdateResponse.from(folder));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/folders/{folderId}")
    public ApplicationResponse<Void> deleteFolder(
            @PathVariable Long folderId,
            @Login AccountCredentials accountCredentials) {

        folderCommandService.deleteFolder(new AccountId(accountCredentials.id().value()), new FolderId(folderId));

        return new ApplicationResponse<>(null);
    }

}
