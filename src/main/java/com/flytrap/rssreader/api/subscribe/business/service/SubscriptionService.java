package com.flytrap.rssreader.api.subscribe.business.service;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderValidator;
import com.flytrap.rssreader.api.subscribe.domain.FolderSubscription;
import com.flytrap.rssreader.api.subscribe.domain.FolderSubscriptionId;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.FolderSubscriptionCommand;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.FolderSubscriptionValidator;
import com.flytrap.rssreader.global.exception.domain.DuplicateDomainException;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final FolderValidator folderValidator;
    private final FolderSubscriptionValidator folderSubscriptionValidator;
    private final FolderSubscriptionCommand folderSubscriptionCommand;

    public FolderSubscription addSubscriptionToFolder(
        AccountId accountId, FolderId folderId, String rssUrl
    ) {
        if (!folderValidator.isAccessibleFolder(folderId, accountId))
            throw new ForbiddenAccessFolderException(Folder.class);

        if (folderSubscriptionValidator.existsBy(folderId, rssUrl))
            throw new DuplicateDomainException(FolderSubscription.class);

        return folderSubscriptionCommand.createFrom(folderId, rssUrl);
    }

    public void removeSubscriptionToFolder(
        AccountId accountId, FolderId folderId, FolderSubscriptionId folderSubscriptionId
    ) {
        if (!folderValidator.isAccessibleFolder(folderId, accountId))
            throw new ForbiddenAccessFolderException(Folder.class);

        folderSubscriptionCommand.delete(folderSubscriptionId);
    }
}
