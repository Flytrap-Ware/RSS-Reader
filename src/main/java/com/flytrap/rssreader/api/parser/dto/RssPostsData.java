package com.flytrap.rssreader.api.parser.dto;

import java.time.Instant;
import java.util.List;

public record RssPostsData(
    String subscribeTitle,
    List<RssItemData> itemData
) {

    public int getItemSize() {
        return itemData.size();
    }

    public record RssItemData(
        String guid,
        String title,
        String description,
        Instant pubDate,
        String thumbnailUrl
    ) {

    }

}
