package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.subscribe.Subscribe;
import jakarta.validation.constraints.Size;

/**
 * 로그인 후 Rss구독할 URL입니다.
 */
public record SubscribeRequest() {

    public record CreateRequest(@Size(min = 1, max = 2500) String blogUrl) {

    }

    public record Response(Long subscribeId) {

        public static SubscribeRequest.Response from(
                Subscribe subscribe) {
            return new SubscribeRequest.Response(subscribe.getId());
        }
    }
}