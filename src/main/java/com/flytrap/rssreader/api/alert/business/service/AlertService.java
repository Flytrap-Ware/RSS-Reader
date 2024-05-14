package com.flytrap.rssreader.api.alert.business.service;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.domain.AlertCreate;
import com.flytrap.rssreader.api.alert.domain.AlertId;
import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.alert.infrastructure.implement.AlertCommand;
import com.flytrap.rssreader.api.alert.infrastructure.implement.AlertQuery;
import com.flytrap.rssreader.api.alert.infrastructure.implement.AlertValidator;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderValidator;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertQuery alertQuery;
    private final AlertCommand alertCommand;
    private final AlertValidator alertValidator;
    private final FolderValidator folderValidator;

    public List<Alert> getAlertsByFolder(FolderId folderId, AccountId accountId) {

        if (!folderValidator.isAccessibleFolder(folderId, accountId))
            throw new ForbiddenAccessFolderException(Folder.class);

        return alertQuery.readAllByFolder(folderId);
    }

    public Alert registerAlert(FolderId folderId, AccountId accountId, String webhookUrl) {

        if (!folderValidator.isAccessibleFolder(folderId, accountId))
            throw new ForbiddenAccessFolderException(Folder.class);

        AlertPlatform alertPlatform = AlertPlatform.parseWebhookUrl(webhookUrl);

        return alertCommand.create(new AlertCreate(folderId, accountId, alertPlatform, webhookUrl));
    }

    public void removeAlert(FolderId folderId, AccountId accountId, AlertId alertId) {

        if (!folderValidator.isAccessibleFolder(folderId, accountId))
            throw new ForbiddenAccessFolderException(Folder.class);

        if (!alertValidator.exists(alertId))
            throw new NoSuchDomainException(Alert.class);

        alertCommand.delete(alertId);
    }

}

