package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.admin.domain.AdminSystemAggregate;
import com.flytrap.rssreader.api.admin.infrastructure.implementation.AdminSystemCommand;
import com.flytrap.rssreader.api.admin.infrastructure.system.PostCollectionEnableLoader;
import com.flytrap.rssreader.api.post.infrastructure.system.PostCollectSystem;
import java.time.Duration;
import java.util.concurrent.ScheduledFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCollectScheduledService implements CommandLineRunner {

    private ScheduledFuture<?> postCollectionScheduledFuture;

    private final PostCollectSystem postCollectSystem;
    private final PostCollectionEnableLoader postCollectionEnableLoader;
    private final AdminSystemCommand adminSystemCommand;
    private final TaskScheduler postCollectionTaskScheduler;

    @Override
    public void run(String... args) throws Exception {
        AdminSystemAggregate adminSystemAggregate = adminSystemCommand.read();
        int batchSize = adminSystemAggregate.getPostCollectionBatchSize();
        long delay = adminSystemAggregate.getPostCollectionDelay();

        Runnable task = () -> this.schedulePostCollection(batchSize);
        postCollectionScheduledFuture = postCollectionTaskScheduler
            .scheduleWithFixedDelay(task, Duration.ofMillis(delay));
    }

    public void restartPostCollection() {
        AdminSystemAggregate adminSystemAggregate = adminSystemCommand.read();
        int batchSize = adminSystemAggregate.getPostCollectionBatchSize();
        long delay = adminSystemAggregate.getPostCollectionDelay();

        if (postCollectionScheduledFuture != null) {
            postCollectionScheduledFuture.cancel(true);
        }

        Runnable task = () -> this.schedulePostCollection(batchSize);
        postCollectionScheduledFuture = postCollectionTaskScheduler
            .scheduleWithFixedDelay(task, Duration.ofMillis(delay));
    }

    public void schedulePostCollection(int collectionBatchSize) {

        if (postCollectionEnableLoader.isDisabled()) {
            return;
        }
        postCollectSystem.loadAndEnqueueRssResources(collectionBatchSize);
    }

    /**
     * 게시글 수집(schedulePostCollection)과 게시글 저장(schedulePostPersistence)
     * Scheduled을 따로 쓰는 이유:
     * 기존에 저장된 RSS 문서의 게시글 수집은 딜레이가 있어도 되지만
     * 회원이 신규 RSS 문서 추가하면 추가한 즉시 해당 문서의 게시글을 저장해서 회원에게 보여줘야 되기 때문입니다.
     * 따라서 게시글 저장(schedulePostPersistence)은 1000ms 마다 실행됩니다.
     */
    @Scheduled(fixedDelay = 1000)
    public void schedulePostPersistence() {

        if (postCollectionEnableLoader.isDisabled()) {
            return;
        }

        postCollectSystem.dequeueAndSaveRssResource();
    }

}
