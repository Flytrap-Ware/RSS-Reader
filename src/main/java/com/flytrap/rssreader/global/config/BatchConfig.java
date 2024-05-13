package com.flytrap.rssreader.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    public static final String JOB_CRAWLING_NAME = "crawlingJob";
    public static final String JOB_STAT_NAME = "statJob";

    @Bean
    public Job crawlingJob(Step crawlingStep) {
        return new JobBuilder(JOB_CRAWLING_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(crawlingStep)
                .end()
                .build();
    }

    @Bean
    public Job statJob(Step statStep) {
        return new JobBuilder(JOB_STAT_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(statStep)
                .end()
                .build();
    }
}
