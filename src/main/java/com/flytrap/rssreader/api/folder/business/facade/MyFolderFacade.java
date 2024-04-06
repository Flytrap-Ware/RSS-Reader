package com.flytrap.rssreader.api.folder.business.facade;

import com.flytrap.rssreader.api.folder.business.service.FolderReadService;
import com.flytrap.rssreader.api.folder.business.service.SharedFolderReadService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MyFolderFacade {

    private final FolderReadService folderReadService;
    private final SharedFolderReadService sharedFolderReadService;

    public List<Folder> getMyFolders(long id) {
        List<Folder> myFolders = folderReadService.findAllByMemberId(id);

        // 내가 초대받은 폴더 목록
        List<Long> invitedFolderIds = sharedFolderReadService.findFoldersInvited(id);
        List<Folder> invitedFolders = folderReadService.findAllByIds(invitedFolderIds);

        myFolders.addAll(invitedFolders);

        return myFolders;
    }
}
