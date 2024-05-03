package com.flytrap.rssreader.api.folder.presentation.controller;

import com.flytrap.rssreader.api.auth.presentation.dto.AccountSession;
import com.flytrap.rssreader.api.folder.business.service.FolderReadService;
import com.flytrap.rssreader.api.folder.business.service.FolderSubscribeService;
import com.flytrap.rssreader.api.folder.business.service.FolderVerifyService;
import com.flytrap.rssreader.api.folder.domain.AccessibleFolders;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.presentation.controller.swagger.FolderReadControllerApi;
import com.flytrap.rssreader.api.folder.presentation.dto.MyFoldersResponse;
import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.subscribe.business.service.SubscribeService;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import com.flytrap.rssreader.api.subscribe.presentation.dto.SubscribeRequest;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class FolderReadController implements FolderReadControllerApi {

    private final FolderVerifyService folderVerifyService;
    private final SubscribeService subscribeService;
    private final FolderSubscribeService folderSubscribeService;

    private final FolderReadService folderReadService;

    @GetMapping
    public ApplicationResponse<MyFoldersResponse> getMyFolders(@Login AccountSession accountSession) {

        AccessibleFolders accessibleFolders = folderReadService.getMyFolders(new AccountId(accountSession.id()));

        return new ApplicationResponse<>(MyFoldersResponse.from(accessibleFolders));
    }

    // TODO: Subscription으로 이동
    @GetMapping("/{folderId}/rss")
    public ApplicationResponse<SubscribeRequest.ResponseList> read( // TODO: 폴더에 구독된 블로그 목록 불러오기 API인데 이름 변경했으면 좋겠어요.
            @PathVariable Long folderId,
            @Login AccountSession member) {

        Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, member.id());
        List<Long> list = folderSubscribeService.getFolderSubscribeId(verifiedFolder.getId());
        List<Subscribe> subscribeList = subscribeService.read(list);

        return new ApplicationResponse<>(SubscribeRequest.ResponseList.from(subscribeList));
    }

}
