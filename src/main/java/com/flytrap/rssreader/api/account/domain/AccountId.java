package com.flytrap.rssreader.api.account.domain;

public record AccountId(
        long value
) {
    public AccountId {
        if (value < 0) {
            throw new IllegalArgumentException("Account id must be positive");
        }
    }
}
