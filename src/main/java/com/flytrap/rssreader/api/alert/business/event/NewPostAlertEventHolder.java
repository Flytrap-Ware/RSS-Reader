package com.flytrap.rssreader.api.alert.business.event;

import com.flytrap.rssreader.global.event.EventHolder;
import lombok.Getter;

public record NewPostAlertEventHolder(@Getter NewPostAlertEventParam value) implements EventHolder<NewPostAlertEventParam> {
}
