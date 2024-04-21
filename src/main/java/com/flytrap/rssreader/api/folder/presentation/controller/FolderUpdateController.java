package com.flytrap.rssreader.api.folder.presentation.controller;

import com.flytrap.rssreader.api.auth.presentation.dto.SessionAccount;
import com.flytrap.rssreader.api.folder.business.service.FolderSubscribeService;
import com.flytrap.rssreader.api.folder.business.service.FolderUpdateService;
import com.flytrap.rssreader.api.folder.business.service.FolderVerifyService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.domain.FolderSubscribe;
import com.flytrap.rssreader.api.folder.domain.MyOwnFolder;
import com.flytrap.rssreader.api.folder.presentation.controller.swagger.FolderUpdateControllerApi;
import com.flytrap.rssreader.api.folder.presentation.dto.FolderUpdateRequest;
import com.flytrap.rssreader.api.folder.presentation.dto.FolderUpdateResponse;
import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.post.business.facade.OpenCheckFacade;
import com.flytrap.rssreader.api.post.business.service.collect.PostCollectService;
import com.flytrap.rssreader.api.subscribe.business.service.SubscribeService;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import com.flytrap.rssreader.api.subscribe.presentation.dto.SubscribeRequest;
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
    private final SubscribeService subscribeService;
    private final FolderSubscribeService folderSubscribeService;
    private final OpenCheckFacade openCheckFacade;
    private final PostCollectService postCollectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<FolderUpdateResponse> createNewFolder(
            @Valid @RequestBody FolderUpdateRequest request,
            @Login SessionAccount accountSession) {

        MyOwnFolder newFolder = folderUpdateService
            .createNewFolder(new AccountId(accountSession.id().value()), request.name());

        return new ApplicationResponse<>(FolderUpdateResponse.from(newFolder));
    }

    @PatchMapping("/{folderId}")
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<FolderUpdateResponse> updateFolder(
            @Valid @RequestBody FolderUpdateRequest request,
            @PathVariable Long folderId,
            @Login SessionAccount accountSession) {

        MyOwnFolder newFolder = folderUpdateService
            .updateFolder(new AccountId(accountSession.id().value()), new FolderId(folderId), request.name());

        return new ApplicationResponse<>(FolderUpdateResponse.from(newFolder));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{folderId}")
    public ApplicationResponse<Void> deleteFolder(
            @PathVariable Long folderId,
            @Login SessionAccount accountSession) {

        folderUpdateService.deleteFolder(new AccountId(accountSession.id().value()), new FolderId(folderId));

        return new ApplicationResponse<>(null);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{folderId}/rss")
    public ApplicationResponse<SubscribeRequest.Response> subscribe(
            @PathVariable Long folderId,
            @Valid @RequestBody SubscribeRequest.CreateRequest request,
            @Login SessionAccount member) {

        Folder verifiedFolder = folderVerifyService.getVerifiedAccessableFolder(folderId, member.id().value());
        Subscribe subscribe = subscribeService.subscribe(request);
        folderSubscribeService.folderSubscribe(subscribe, verifiedFolder.getId());

        if (subscribe.isNewSubscribe()) {
            postCollectService.addNewSubscribeForCollect(subscribe);
        }

        FolderSubscribe folderSubscribe = FolderSubscribe.from(subscribe);
        folderSubscribe = openCheckFacade.addUnreadCountInFolderSubscribe(member.id().value(), subscribe, folderSubscribe);

        return new ApplicationResponse<>(SubscribeRequest.Response.from(folderSubscribe));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{folderId}/rss/{subscribeId}")
    public ApplicationResponse<Void> unsubscribe(
            @PathVariable Long folderId,
            @PathVariable Long subscribeId,
            @Login SessionAccount member) {

        Folder verifiedFolder = folderVerifyService.getVerifiedAccessableFolder(folderId, member.id().value());
        folderSubscribeService.folderUnsubscribe(subscribeId,
                verifiedFolder.getId());
        return new ApplicationResponse<>(null);
    }
}
