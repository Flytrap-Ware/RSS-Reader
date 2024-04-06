package com.flytrap.rssreader.api.folder.presentation.dto;

import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.SharedFolder;
import com.flytrap.rssreader.api.member.presentation.dto.response.MemberSummary;
import com.flytrap.rssreader.api.subscribe.presentation.dto.SubscribeListSummary;

import java.util.List;

public record FolderListSummary(long id,
                                String name,
                                int unreadCount,
                                List<SubscribeListSummary> blogs,
                                List<MemberSummary> invitedMembers) {

    public static FolderListSummary from(Folder folder) {

        if (folder instanceof SharedFolder) {
            return new FolderListSummary(folder.getId(),
                    folder.getName(),
                    folder.getUnreadCount(),
                    SubscribeListSummary.from(folder.getSubscribes()),
                    MemberSummary.from(((SharedFolder) folder).getInvitedMembers()));
        }

        return new FolderListSummary(folder.getId(),
                folder.getName(),
                folder.getUnreadCount(),
                SubscribeListSummary.from(folder.getSubscribes()),
                List.of());
    }
}
