package com.flytrap.rssreader.global.config;

import com.flytrap.rssreader.api.post.business.service.collect.PostCollectService;
import com.flytrap.rssreader.global.batch.tasklet.CrawlingTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class CrawlingJobConfig {

    public static final String JOB_CRAWLING_NAME = "crawlingJob";

    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final PostCollectService postCollectService;
    public static final String STEP_CRAWLING_NAME = "crawlingStep";

    @Bean
    public Job crawlingJob(Step crawlingStep) {
        return new JobBuilder(JOB_CRAWLING_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(crawlingStep)
                .end()
                .build();
    }

    @Bean
    public Step crawlingStep() {
        return new StepBuilder(STEP_CRAWLING_NAME, jobRepository)
                .tasklet(new CrawlingTasklet(postCollectService), transactionManager)
                .build();
    }
}
