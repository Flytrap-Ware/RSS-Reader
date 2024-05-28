package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.AccessibleFolders;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderQueryService {

    private final FolderQuery folderQuery;

    @Transactional(readOnly = true)
    public AccessibleFolders getMyFolders(AccountId accountId) {
        List<Folder> folders = folderQuery.readAllByAccount(accountId);

        return new AccessibleFolders(
            folders.stream().filter(Folder::isPrivate).toList(),
            folders.stream().filter(Folder::isShared).toList()
        );
    }

}
