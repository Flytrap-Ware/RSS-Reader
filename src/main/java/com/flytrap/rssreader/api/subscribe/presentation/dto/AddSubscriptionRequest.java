package com.flytrap.rssreader.api.subscribe.presentation.dto;

import jakarta.validation.constraints.Size;

public record AddSubscriptionRequest(
    @Size(min = 1, max = 2500) String blogUrl // TODO: rssurl로 변경하기 + fornt도 함께
) {

}
