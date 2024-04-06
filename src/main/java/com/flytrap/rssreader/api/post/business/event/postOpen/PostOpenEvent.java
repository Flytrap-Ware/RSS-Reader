package com.flytrap.rssreader.api.post.business.event.postOpen;

import com.flytrap.rssreader.global.event.EventHolder;
import lombok.Getter;

public record PostOpenEvent(@Getter PostOpenEventParam value) implements EventHolder<PostOpenEventParam> {
}
