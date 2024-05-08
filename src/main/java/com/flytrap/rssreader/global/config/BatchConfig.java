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

    @Bean
    public Job crawlingJob(Step crawlingStep) {
        return new JobBuilder("crawlingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(crawlingStep)
                .end()
                .build();
    }

    @Bean
    public Job statJob(Step statStep) {
        return new JobBuilder("statJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(statStep)
                .end()
                .build();
    }
}
