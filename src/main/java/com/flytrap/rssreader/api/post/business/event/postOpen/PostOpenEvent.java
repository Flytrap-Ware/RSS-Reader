package com.flytrap.rssreader.api.post.business.event.postOpen;

import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.post.domain.PostAggregate;

public record PostOpenEvent(
    PostAggregate postAggregate,
    AccountId accountId
)  {
}
