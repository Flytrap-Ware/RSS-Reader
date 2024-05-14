package com.flytrap.rssreader.api.subscribe.business.service;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderValidator;
import com.flytrap.rssreader.api.subscribe.domain.Subscription;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.SubscriptionCommand;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.SubscriptionValidator;
import com.flytrap.rssreader.global.exception.domain.DuplicateDomainException;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final FolderValidator folderValidator;
    private final SubscriptionValidator subscriptionValidator;
    private final SubscriptionCommand subscriptionCommand;

    public Subscription addSubscriptionToFolder(
        AccountId accountId, FolderId folderId, String rssUrl
    ) {
        if (!folderValidator.isAccessibleFolder(folderId, accountId))
            throw new ForbiddenAccessFolderException(Folder.class);

        if (subscriptionValidator.existsBy(folderId, rssUrl))
            throw new DuplicateDomainException(Subscription.class);

        return subscriptionCommand.createFrom(folderId, rssUrl);
    }

    public void removeSubscriptionToFolder(
        AccountId accountId, FolderId folderId, SubscriptionId subscriptionId
    ) {
        if (!folderValidator.isAccessibleFolder(folderId, accountId))
            throw new ForbiddenAccessFolderException(Folder.class);

        subscriptionCommand.delete(subscriptionId);
    }
}
