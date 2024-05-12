package com.flytrap.rssreader.global.config;

import com.flytrap.rssreader.api.post.business.service.collect.PostCollectService;
import com.flytrap.rssreader.api.post.domain.PostBlogPlatformData;
import com.flytrap.rssreader.api.post.domain.PostStat;
import com.flytrap.rssreader.global.batch.step.StatItemProcessor;
import com.flytrap.rssreader.global.batch.tasklet.CrawlingTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class StepConfig {

    public static final int CHUNK_SIZE = 1;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final PostCollectService postCollectService;
    private final JpaCursorItemReader<PostBlogPlatformData> jpaCursorItemReader;
    private final JpaItemWriter<PostStat> jpaItemWriter;

    @Bean
    public Step crawlingStep() {
        return new StepBuilder("crawlingStep", jobRepository)
                .tasklet(new CrawlingTasklet(postCollectService), transactionManager)
                .build();
    }

    @Bean
    public Step statStep() {
        return new StepBuilder("crawlingStep", jobRepository)
                .<PostBlogPlatformData, PostStat>chunk(CHUNK_SIZE, transactionManager)
                .reader(jpaCursorItemReader)
                .processor(processor())
                .writer(jpaItemWriter)
                .build();
    }

    @Bean
    public ItemProcessor<PostBlogPlatformData, PostStat> processor() {
        return new StatItemProcessor();
    }
}
