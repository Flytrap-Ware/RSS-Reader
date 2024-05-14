package com.flytrap.rssreader.api.parser.dto;

import java.time.Instant;
import java.util.List;

public record RssPostsData(
    String rssSourceTitle,
    List<RssItemData> itemData
) {

    public record RssItemData(
        String guid,
        String title,
        String description,
        Instant pubDate,
        String thumbnailUrl
    ) {

    }

}
