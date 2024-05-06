package com.flytrap.rssreader.api.batch.tasklet;

import com.flytrap.rssreader.api.post.business.service.collect.PostCollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlingTasklet implements Tasklet {

    @Value("${collector.subscribe-queue.select-batch-size}")
    private int selectBatchSize;
    private final PostCollectService postCollectService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        //TODO: 왜인지 모르겠지만 @Value를 못 읽는다 추후 수정 필요
        postCollectService.collectPosts(50);
        return RepeatStatus.FINISHED;
    }
}
