package com.flytrap.rssreader.api.post.business.event;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.post.domain.PostAggregate;

public record PostOpenEvent(
    PostAggregate postAggregate,
    AccountId accountId
)  {
}
