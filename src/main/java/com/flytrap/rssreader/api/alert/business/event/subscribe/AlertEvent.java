package com.flytrap.rssreader.api.alert.business.event.subscribe;

import com.flytrap.rssreader.global.event.EventHolder;
import com.flytrap.rssreader.api.alert.business.service.dto.AlertParam;
import lombok.Getter;

public record AlertEvent(@Getter AlertParam value) implements EventHolder<AlertParam> {}
