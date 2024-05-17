package com.flytrap.rssreader.global.batch.step;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostStatEntity;
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
    public JpaItemWriter<PostStatEntity> jpaItemWriter() {
        JpaItemWriter<PostStatEntity> writer = new JpaItemWriter<>() {
            @Override
            public void write(Chunk<? extends PostStatEntity> items) {
                log.debug("[WRITER] - {}", items);
                super.write(items);
            }
        };
        writer.setEntityManagerFactory(emf);
        return writer;
    }
}
