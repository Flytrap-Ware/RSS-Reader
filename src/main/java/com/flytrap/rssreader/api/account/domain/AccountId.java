package com.flytrap.rssreader.api.account.domain;

import java.io.Serializable;

public record AccountId(
        long value
) implements Serializable  {
    public AccountId {
        if (value < 0) {
            throw new IllegalArgumentException("Account id must be positive");
        }
    }
}
