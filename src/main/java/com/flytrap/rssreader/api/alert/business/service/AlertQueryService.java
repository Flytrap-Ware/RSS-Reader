package com.flytrap.rssreader.api.alert.business.service;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.infrastructure.implement.AlertQuery;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderValidator;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertQueryService {

    private final AlertQuery alertQuery;
    private final FolderValidator folderValidator;

    public List<Alert> getAlertsByFolder(FolderId folderId, AccountId accountId) {

        if (!folderValidator.isAccessibleFolder(folderId, accountId))
            throw new ForbiddenAccessFolderException(Folder.class);

        return alertQuery.readAllByFolder(folderId);
    }
}
