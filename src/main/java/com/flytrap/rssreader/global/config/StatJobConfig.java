package com.flytrap.rssreader.global.config;

import com.flytrap.rssreader.api.post.domain.PostBlogPlatformData;
import com.flytrap.rssreader.api.post.domain.PostStat;
import com.flytrap.rssreader.global.batch.step.StatItemProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class StatJobConfig {

    public static final int CHUNK_SIZE = 1;
    public static final String JOB_STAT_NAME = "statJob";
    public static final String STEP_STAT_NAME = "statStep";

    private final JobRepository jobRepository;
    private final JpaCursorItemReader<PostBlogPlatformData> jpaCursorItemReader;
    private final JpaItemWriter<PostStat> jpaItemWriter;
    private final PlatformTransactionManager transactionManager;


    @Bean
    public Job statJob(Step statStep) {
        return new JobBuilder(JOB_STAT_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(statStep)
                .end()
                .build();
    }

    @Bean
    public Step statStep() {
        return new StepBuilder(STEP_STAT_NAME, jobRepository)
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
