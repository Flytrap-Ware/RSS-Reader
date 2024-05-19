package com.flytrap.rssreader.global.batch.tasklet;

import com.flytrap.rssreader.api.post.business.service.PostCollectScheduledService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlingTasklet implements Tasklet {


    private final PostCollectScheduledService postCollectScheduledService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        //TODO: 왜인지 모르겠지만 @Value를 못 읽는다 추후 수정 필요
        postCollectScheduledService.collectPostsScheduled();
        return RepeatStatus.FINISHED;
    }
}
