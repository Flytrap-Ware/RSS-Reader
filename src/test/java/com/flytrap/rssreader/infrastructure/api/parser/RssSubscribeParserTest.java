package com.flytrap.rssreader.infrastructure.api.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.flytrap.rssreader.api.parser.RssSubscribeParser;
import com.flytrap.rssreader.api.subscribe.domain.BlogPlatform;
import com.flytrap.rssreader.api.parser.dto.RssSubscribeData;
import com.flytrap.rssreader.api.subscribe.presentation.dto.SubscribeRequest.CreateRequest;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("RssChecker테스트 -Subscribe")
class RssSubscribeParserTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "https://v2.velog.io/rss/jinny-l",
            "https://jinney.tistory.com/rss"
    })
    void parseRssDocuments_Success(String rssUrl) {
        // given
        CreateRequest createRequest = new CreateRequest(rssUrl);
        RssSubscribeParser rssSubscribeParser = new RssSubscribeParser();

        // when
        Optional<RssSubscribeData> result = rssSubscribeParser.parseRssDocuments(rssUrl);
        BlogPlatform blogPlatform = BlogPlatform.parseLink(createRequest.blogUrl());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertEquals(createRequest.blogUrl(), result.orElseThrow().url());
            Assertions.assertNotNull(result.get().title());
            Assertions.assertNotNull(result.get().description());
            Assertions.assertNotNull(result.get().platform());
            Assertions.assertEquals(blogPlatform, result.get().platform());
        });
    }
}
