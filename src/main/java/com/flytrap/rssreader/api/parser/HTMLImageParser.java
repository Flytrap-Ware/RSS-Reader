package com.flytrap.rssreader.api.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class HTMLImageParser {

    private static final String IMG_TAG_NAME = "img";
    private static final String SRC_ATTRIBUTE_NAME = "src";
    private static final String HTTPS_SCHEME_NAME = "https://";

    public String extractImageUrl(String html) {
        Document doc = Jsoup.parse(html);
        Element imgElement = doc.select(IMG_TAG_NAME).first();

        if (imgElement != null) {
            String imageUrl = imgElement.attr(SRC_ATTRIBUTE_NAME);
            return imageUrl.startsWith(HTTPS_SCHEME_NAME) ? imageUrl : null;
        } else {
            return null;
        }
    }
}
