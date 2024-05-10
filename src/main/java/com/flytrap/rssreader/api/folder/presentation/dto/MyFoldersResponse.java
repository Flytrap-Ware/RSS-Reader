package com.flytrap.rssreader.api.folder.presentation.dto;

import com.flytrap.rssreader.api.folder.domain.AccessibleFolders;
import com.flytrap.rssreader.api.folder.domain.FolderDomain;
import com.flytrap.rssreader.api.folder.domain.SharedMember;
import com.flytrap.rssreader.api.folder.domain.SharedStatus;
import com.flytrap.rssreader.api.subscribe.domain.FolderSubscription;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record MyFoldersResponse(
    Map<SharedStatus, List<MyFolderSummary>> folders
) {

    public static MyFoldersResponse from(AccessibleFolders accessibleFolders) {
        Map<SharedStatus, List<MyFolderSummary>> result = new LinkedHashMap<>();
        result.put(
            SharedStatus.PRIVATE,
            accessibleFolders.privateFolders().stream()
                .map(MyFolderSummary::from).toList());
        result.put(
            SharedStatus.SHARED,
            accessibleFolders.sharedFolder().stream()
                .map(MyFolderSummary::from).toList());

        return new MyFoldersResponse(result);
    }

    public record MyFolderSummary(
        long id,
        String name,
        List<FolderSubscriptionSummary> blogs,
        List<SharedMemberSummary> invitedMembers
    ) {
        public static MyFolderSummary from(FolderDomain folder) {
            return new MyFolderSummary(
                folder.getId().value(),
                folder.getName(),
                folder.getSubscriptions().stream()
                    .map(FolderSubscriptionSummary::from).toList(),
                folder.getSharedMembers().stream()
                    .map(SharedMemberSummary::from).toList()
            );
        }
    }

    public record FolderSubscriptionSummary(
        long id,
        String title
    ) {
        public static FolderSubscriptionSummary from(FolderSubscription folderSubscription) {
            return new FolderSubscriptionSummary(
                folderSubscription.getId().value(),
                folderSubscription.getTitle()
            );
        }
    }

    public record SharedMemberSummary(
        Long id,
        String name,
        String profile
    ) {
        public static SharedMemberSummary from(SharedMember sharedMember) {
            return new SharedMemberSummary(
                sharedMember.getId().value(),
                sharedMember.getName(),
                sharedMember.getProfileUrl()
            );
        }
    }

}
