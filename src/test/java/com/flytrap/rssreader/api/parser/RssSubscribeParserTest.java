package com.flytrap.rssreader.api.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.flytrap.rssreader.api.parser.dto.RssSourceData;
import com.flytrap.rssreader.api.subscribe.domain.BlogPlatform;
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
        RssSubscribeParser rssSubscribeParser = new RssSubscribeParser();

        // when
        Optional<RssSourceData> result = rssSubscribeParser.parseRssDocuments(rssUrl);
        BlogPlatform blogPlatform = BlogPlatform.parseLink(rssUrl);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertEquals(rssUrl, result.orElseThrow().url());
            Assertions.assertNotNull(result.get().title());
            Assertions.assertNotNull(result.get().description());
            Assertions.assertNotNull(result.get().platform());
            Assertions.assertEquals(blogPlatform, result.get().platform());
        });
    }
}
