package com.flytrap.rssreader.api.alert.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record AlertId (
    long value
) implements DomainId {
    public AlertId {
            if (value < 0) {
                throw new IllegalArgumentException("Alert id must be positive");
            }
        }
    }
