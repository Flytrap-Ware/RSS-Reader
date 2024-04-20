package com.flytrap.rssreader.api.folder.presentation.controller;

import com.flytrap.rssreader.api.auth.presentation.dto.AccountSession;
import com.flytrap.rssreader.api.folder.business.service.FolderSubscribeService;
import com.flytrap.rssreader.api.folder.business.service.FolderUpdateService;
import com.flytrap.rssreader.api.folder.business.service.FolderVerifyService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderSubscribe;
import com.flytrap.rssreader.api.folder.presentation.controller.swagger.FolderUpdateControllerApi;
import com.flytrap.rssreader.api.folder.presentation.dto.FolderRequest;
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

    private final FolderUpdateService folderService;
    private final FolderVerifyService folderVerifyService;
    private final SubscribeService subscribeService;
    private final FolderSubscribeService folderSubscribeService;
    private final OpenCheckFacade openCheckFacade;
    private final PostCollectService postCollectService;

    @PostMapping
    public ApplicationResponse<FolderRequest.Response> createFolder(
            @Valid @RequestBody FolderRequest.CreateRequest request,
            @Login AccountSession member) {

        Folder newFolder = folderService.createNewFolder(request, member.id());

        return new ApplicationResponse<>(FolderRequest.Response.from(newFolder));
    }

    @PatchMapping("/{folderId}")
    public ApplicationResponse<FolderRequest.Response> updateFolder(
            @Valid @RequestBody FolderRequest.CreateRequest request,
            @PathVariable Long folderId,
            @Login AccountSession member) {

        Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, member.id());
        Folder updatedFolder = folderService.updateFolder(request, verifiedFolder, member.id());

        return new ApplicationResponse<>(FolderRequest.Response.from(updatedFolder));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{folderId}")
    public ApplicationResponse<String> deleteFolder(
            @PathVariable Long folderId,
            @Login AccountSession member) {

        Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, member.id());
        Folder folder = folderService.deleteFolder(verifiedFolder, member.id());
        folderSubscribeService.unsubscribeAllByFolder(folder);

        return new ApplicationResponse<>("폴더가 삭제되었습니다 : " + folder.getName());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{folderId}/rss")
    public ApplicationResponse<SubscribeRequest.Response> subscribe(
            @PathVariable Long folderId,
            @Valid @RequestBody SubscribeRequest.CreateRequest request,
            @Login AccountSession member) {

        Folder verifiedFolder = folderVerifyService.getVerifiedAccessableFolder(folderId, member.id());
        Subscribe subscribe = subscribeService.subscribe(request);
        folderSubscribeService.folderSubscribe(subscribe, verifiedFolder.getId());

        if (subscribe.isNewSubscribe()) {
            postCollectService.addNewSubscribeForCollect(subscribe);
        }

        FolderSubscribe folderSubscribe = FolderSubscribe.from(subscribe);
        folderSubscribe = openCheckFacade.addUnreadCountInFolderSubscribe(member.id(), subscribe, folderSubscribe);

        return new ApplicationResponse<>(SubscribeRequest.Response.from(folderSubscribe));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{folderId}/rss/{subscribeId}")
    public ApplicationResponse<Void> unsubscribe(
            @PathVariable Long folderId,
            @PathVariable Long subscribeId,
            @Login AccountSession member) {

        Folder verifiedFolder = folderVerifyService.getVerifiedAccessableFolder(folderId, member.id());
        folderSubscribeService.folderUnsubscribe(subscribeId,
                verifiedFolder.getId());
        return new ApplicationResponse<>(null);
    }
}
