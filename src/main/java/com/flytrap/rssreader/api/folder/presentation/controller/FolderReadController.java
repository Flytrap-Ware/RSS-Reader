package com.flytrap.rssreader.api.folder.presentation.controller;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.folder.business.service.FolderReadService;
import com.flytrap.rssreader.api.folder.domain.AccessibleFolders;
import com.flytrap.rssreader.api.folder.presentation.controller.swagger.FolderReadControllerApi;
import com.flytrap.rssreader.api.folder.presentation.dto.MyFoldersResponse;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class FolderReadController implements FolderReadControllerApi {

    private final FolderReadService folderReadService;

    @GetMapping
    public ApplicationResponse<MyFoldersResponse> getMyFolders(@Login AccountCredentials accountSession) {

        AccessibleFolders accessibleFolders = folderReadService.getMyFolders(new AccountId(accountSession.id().value()));

        return new ApplicationResponse<>(MyFoldersResponse.from(accessibleFolders));
    }

}
