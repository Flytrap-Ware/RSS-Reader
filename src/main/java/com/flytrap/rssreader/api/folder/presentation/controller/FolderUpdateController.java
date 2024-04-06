package com.flytrap.rssreader.api.folder.presentation.controller;

import com.flytrap.rssreader.api.alert.business.service.AlertService;
import com.flytrap.rssreader.api.folder.business.service.FolderSubscribeService;
import com.flytrap.rssreader.api.folder.business.service.FolderUpdateService;
import com.flytrap.rssreader.api.folder.business.service.FolderVerifyService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderSubscribe;
import com.flytrap.rssreader.api.folder.presentation.controller.swagger.FolderUpdateControllerApi;
import com.flytrap.rssreader.api.folder.presentation.dto.FolderRequest;
import com.flytrap.rssreader.api.post.business.service.PostCollectService;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.SessionMember;
import com.flytrap.rssreader.api.subscribe.presentation.dto.SubscribeRequest;
import com.flytrap.rssreader.api.post.business.facade.OpenCheckFacade;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import com.flytrap.rssreader.api.subscribe.business.service.SubscribeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class FolderUpdateController implements FolderUpdateControllerApi {

    private final FolderUpdateService folderService;
    private final FolderVerifyService folderVerifyService;
    private final SubscribeService subscribeService;
    private final FolderSubscribeService folderSubscribeService;
    private final PostCollectService postCollectService;
    private final AlertService alertService;
    private final OpenCheckFacade openCheckFacade;

    @PostMapping
    public ApplicationResponse<FolderRequest.Response> createFolder(
            @Valid @RequestBody FolderRequest.CreateRequest request,
            @Login SessionMember member) {

        Folder newFolder = folderService.createNewFolder(request, member.id());

        return new ApplicationResponse<>(FolderRequest.Response.from(newFolder));
    }

    @PatchMapping("/{folderId}")
    public ApplicationResponse<FolderRequest.Response> updateFolder(
            @Valid @RequestBody FolderRequest.CreateRequest request,
            @PathVariable Long folderId,
            @Login SessionMember member) {

        Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, member.id());
        Folder updatedFolder = folderService.updateFolder(request, verifiedFolder, member.id());

        return new ApplicationResponse<>(FolderRequest.Response.from(updatedFolder));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{folderId}")
    public ApplicationResponse<String> deleteFolder(
            @PathVariable Long folderId,
            @Login SessionMember member) {

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
            @Login SessionMember member) {
        //TODO: 존재하는 폴더인지 검증하는 로직 , 공유폴더초대받은 인지 검증
        //TODO: 내가만든 폴더랑, 초대받은 그룹의 폴더인지 구분해서 구독해야합니다.
        //지금 부분은 검증된 개인 폴더입니다.
        Folder verifiedFolder = folderVerifyService.getVerifiedAccessableFolder(folderId, member.id());
        Subscribe subscribe = subscribeService.subscribe(request);
        folderSubscribeService.folderSubscribe(subscribe,
                verifiedFolder.getId());

        if (subscribe.isNewSubscribe()) {
            postCollectService.collectPostsFromNewSubscribe(subscribe);
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
            @Login SessionMember member) {

        Folder verifiedFolder = folderVerifyService.getVerifiedAccessableFolder(folderId, member.id());
        folderSubscribeService.folderUnsubscribe(subscribeId,
                verifiedFolder.getId());
        return new ApplicationResponse<>(null);
    }
}
