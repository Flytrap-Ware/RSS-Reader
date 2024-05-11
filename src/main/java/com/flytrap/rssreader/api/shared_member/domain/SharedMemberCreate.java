package com.flytrap.rssreader.api.shared_member.domain;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderId;

public record SharedMemberCreate(
    FolderId folderId,
    AccountId inviteeId
) {

}
