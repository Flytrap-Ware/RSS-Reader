package com.flytrap.rssreader.global.batch.step;

import com.flytrap.rssreader.api.post.domain.PostStat;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatItemWriter {

    private final EntityManagerFactory emf;

    @Bean
    public JpaItemWriter<PostStat> jpaItemWriter() {
        JpaItemWriter<PostStat> writer = new JpaItemWriter<>() {
            @Override
            public void write(Chunk<? extends PostStat> items) {
                log.debug("[WRITER] - {}", items);
                super.write(items);
            }
        };
        writer.setEntityManagerFactory(emf);
        return writer;
    }
}
