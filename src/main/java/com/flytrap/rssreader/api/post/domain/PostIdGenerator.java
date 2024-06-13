package com.flytrap.rssreader.api.post.domain;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PostIdGenerator {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String generateString(Instant pubDate, long rssSourceId) {
        ZonedDateTime zonedDateTime = pubDate.atZone(ZoneOffset.UTC);

        return zonedDateTime.format(formatter) + "-" + rssSourceId;
    }

}
