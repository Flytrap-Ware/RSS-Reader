package com.flytrap.rssreader.global.batch.step;

import com.flytrap.rssreader.api.post.domain.PostBlogPlatformData;
import com.flytrap.rssreader.api.post.domain.PostStat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


@Component
@RequiredArgsConstructor
@Slf4j
public class StatItemProcessor implements ItemProcessor<PostBlogPlatformData, PostStat> {

    @Override
    public PostStat process(PostBlogPlatformData data) {
        Instant now = Instant.now();
        ZonedDateTime localTime = now.atZone(ZoneId.of("Asia/Seoul"));
        return PostStat.builder()
                .platform(data.getPlatform())
                .postCount(data.getPostCount())
                .pubDate(localTime.toInstant())
                .build();
    }
}
