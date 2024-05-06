package com.flytrap.rssreader.global.config;

import com.flytrap.rssreader.api.batch.tasklet.CrawlingTasklet;
import com.flytrap.rssreader.api.post.business.service.collect.PostCollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class CrawlingStepConfig {

    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final PostCollectService postCollectService;

    @Bean
    public Step crawlingStep() {
        return new StepBuilder("crawlingStep", jobRepository)
                .tasklet(new CrawlingTasklet(postCollectService), transactionManager)
                .build();
    }
}
