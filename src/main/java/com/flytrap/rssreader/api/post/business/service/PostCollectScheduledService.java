package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.admin.infrastructure.implementation.AdminSystemCommand;
import com.flytrap.rssreader.api.admin.infrastructure.system.PostCollectionEnableLoader;
import com.flytrap.rssreader.api.post.infrastructure.system.PostCollectSystem;
import java.time.Duration;
import java.util.concurrent.ScheduledFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCollectScheduledService implements CommandLineRunner {

    @Value("${collector.subscribe-queue.select-batch-size}")
    private int selectBatchSize;
    private ScheduledFuture<?> postCollectionScheduledFuture;

    private final PostCollectSystem postCollectSystem;
    private final PostCollectionEnableLoader postCollectionEnableLoader;
    private final AdminSystemCommand adminSystemCommand;
    private final TaskScheduler taskScheduler;

    @Override
    public void run(String... args) throws Exception {
        long fixedDelayFromDb = adminSystemCommand.read().getPostCollectionDelay();

        Runnable task = this::schedulePostCollection;
        postCollectionScheduledFuture = taskScheduler
            .scheduleWithFixedDelay(task, Duration.ofMillis(fixedDelayFromDb));
    }

    public void changePostCollectionDelay(int delay) {
        postCollectionScheduledFuture.cancel(true);

        Runnable task = this::schedulePostCollection;
        postCollectionScheduledFuture = taskScheduler.scheduleWithFixedDelay(task, Duration.ofMillis(delay));
    }

    public void schedulePostCollection() {

        if (postCollectionEnableLoader.isDisabled()) {
            return;
        }

        postCollectSystem.loadAndEnqueueRssResources(selectBatchSize);
    }

    @Scheduled(fixedDelay = 1000)
    public void schedulePostPersistence() {

        if (postCollectionEnableLoader.isDisabled()) {
            return;
        }

        postCollectSystem.dequeueAndSaveRssResource();
    }

}
