package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.shared_member.domain.SharedMember;
import com.flytrap.rssreader.api.subscribe.domain.FolderSubscription;
import com.flytrap.rssreader.global.model.NewDefaultDomain;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class Folder implements NewDefaultDomain {
    private final FolderId id;
    private final String name;
    private final AccountId ownerId;
    private final SharedStatus sharedStatus;
    private final List<FolderSubscription> subscriptions;
    private final List<SharedMember> sharedMembers;

    public boolean isShared() {
        return sharedStatus == SharedStatus.SHARED;
    }

    public boolean isPrivate() {
        return sharedStatus == SharedStatus.PRIVATE;
    }
}
