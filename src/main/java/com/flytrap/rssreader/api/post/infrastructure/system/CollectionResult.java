package com.flytrap.rssreader.api.post.infrastructure.system;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CollectionResult {
    private int rssCount;
    private int postCount;
    private int parsingFailureCount;
}
