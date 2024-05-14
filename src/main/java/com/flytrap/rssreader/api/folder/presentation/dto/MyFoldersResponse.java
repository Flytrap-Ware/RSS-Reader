package com.flytrap.rssreader.api.folder.presentation.dto;

import com.flytrap.rssreader.api.folder.domain.AccessibleFolders;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.shared_member.domain.SharedMember;
import com.flytrap.rssreader.api.folder.domain.SharedStatus;
import com.flytrap.rssreader.api.subscribe.domain.Subscription;
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
        List<SubscriptionSummary> blogs,
        List<SharedMemberSummary> invitedMembers
    ) {
        public static MyFolderSummary from(Folder folder) {
            return new MyFolderSummary(
                folder.getId().value(),
                folder.getName(),
                folder.getSubscriptions().stream()
                    .map(SubscriptionSummary::from).toList(),
                folder.getSharedMembers().stream()
                    .map(SharedMemberSummary::from).toList()
            );
        }
    }

    public record SubscriptionSummary(
        long id,
        String title
    ) {
        public static SubscriptionSummary from(Subscription subscription) {
            return new SubscriptionSummary(
                subscription.getId().value(),
                subscription.getTitle()
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
