package com.flytrap.rssreader.api.alert.domain;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderId;

public record AlertCreate(
    FolderId folderId,
    AccountId accountId,
    AlertPlatform alertPlatform,
    String webhookUrl
) {

}
