package com.flytrap.rssreader.api.subscribe.presentation.dto;

import com.flytrap.rssreader.api.folder.domain.FolderSubscribe;

import java.util.List;

public record SubscribeListSummary(long id,
                                   String title,
                                   int unreadCount) {

    public static List<SubscribeListSummary> from(List<FolderSubscribe> subscribes)
    {
        return subscribes.stream()
                .map(e -> new SubscribeListSummary(e.getId(),
                        e.getTitle(),
                        e.getUnreadCount()))
                .toList();
    }
}
