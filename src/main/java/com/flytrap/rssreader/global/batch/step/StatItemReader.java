package com.flytrap.rssreader.global.batch.step;

import com.flytrap.rssreader.api.post.domain.PostBlogPlatformData;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class StatItemReader {

    private final EntityManagerFactory emf;

    @Bean
    public JpaCursorItemReader<PostBlogPlatformData> jpaCursorItemReader() {
        JpaCursorItemReader<PostBlogPlatformData> reader = new JpaCursorItemReader<>() {
            @Override
            protected PostBlogPlatformData doRead() {
                var post = super.doRead();
                if (post != null)
                    log.debug("[READER] - {}", post);
                return post;
            }
        };

        reader.setName("jpaCursorItemReader");
        reader.setEntityManagerFactory(emf);
        reader.setQueryString("SELECT NEW com.flytrap.rssreader.api.post.domain.PostBlogPlatformData(s.platform, COUNT(*)) " +
                "FROM PostEntity p " +
                "JOIN SubscribeEntity s ON s.id = p.subscribe.id " +
                "GROUP BY s.platform ");
        return reader;
    }
}
