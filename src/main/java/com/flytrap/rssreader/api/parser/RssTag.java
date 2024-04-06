package com.flytrap.rssreader.api.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RssTag {
    RSS("rss"), ATOM("feed");

    private final String rootTagName;

    @Getter
    @AllArgsConstructor
    public enum RssSubscribeTag {
        CHANNEL("channel"), TITLE("title"),
        DESCRIPTION("description"), ITEM("item");

        private final String tagName;
    }

    @Getter
    @AllArgsConstructor
    public enum RssItemTag {
        GUID("guid"), TITLE("title"),
        DESCRIPTION("description"), CONTENT("content:encoded"),
        PUB_DATE("pubDate");

        private final String tagName;
    }

    @Getter
    @AllArgsConstructor
    public enum AtomSubscribeTag {
        FEED("feed"), TITLE("title"), ENTRY("entry");

        private final String tagName;
    }

    @Getter
    @AllArgsConstructor
    public enum AtomEntryTag {
        ID("id"), TITLE("title"),
        CONTENT("content"), UPDATED("updated");

        private final String tagName;
    }

}
