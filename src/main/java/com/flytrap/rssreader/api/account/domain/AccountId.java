package com.flytrap.rssreader.api.account.domain;

import com.flytrap.rssreader.global.model.DomainId;
import java.io.Serializable;

public record AccountId(
        long value
) implements DomainId, Serializable  {
    public AccountId {
        if (value < 0) {
            throw new IllegalArgumentException("Account id must be positive");
        }
    }
}
