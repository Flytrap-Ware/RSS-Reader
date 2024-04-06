package com.flytrap.rssreader.api.subscribe.presentation.dto;

import com.flytrap.rssreader.api.folder.domain.FolderSubscribe;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 로그인 후 RSS구독할 URL입니다.
 */
public record SubscribeRequest() {

    public record CreateRequest(@Size(min = 1, max = 2500) String blogUrl) {

    }

    public record Response(Long subscribeId, String subscribeTitle, Integer unreadCount) {

        public static SubscribeRequest.Response from(
            FolderSubscribe folderSubscribe) {
            return new SubscribeRequest.Response(folderSubscribe.getId(),
                folderSubscribe.getTitle(), folderSubscribe.getUnreadCount());
        }
    }

    public record ResponseList(List<Subscribe> subscribeList) {

        public static SubscribeRequest.ResponseList from(List<Subscribe> subscribeList) {
            return new SubscribeRequest.ResponseList(subscribeList);
        }
    }
}
