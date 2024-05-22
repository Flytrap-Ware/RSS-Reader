package com.flytrap.rssreader.api.admin.business.service;

import com.flytrap.rssreader.api.admin.domain.AdminSystemAggregate;
import com.flytrap.rssreader.api.admin.infrastructure.implementation.AdminSystemCommand;
import com.flytrap.rssreader.api.admin.infrastructure.system.PostCollectionEnableLoader;
import com.flytrap.rssreader.api.post.business.service.PostCollectScheduledService;
import com.flytrap.rssreader.api.post.infrastructure.system.PostCollectSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminSystemService {

    private final AdminSystemCommand adminSystemCommand;
    private final PostCollectionEnableLoader postCollectionEnableLoader;
    private final PostCollectSystem postCollectSystem;
    private final PostCollectScheduledService postCollectScheduledService;

    public void startPostCollection() {
        AdminSystemAggregate adminSystemAggregate = adminSystemCommand.read();
        adminSystemAggregate.startPostCollection();

        adminSystemCommand.save(adminSystemAggregate);
        postCollectionEnableLoader.toEnable();
    }

    public void stopPostCollection() {
        AdminSystemAggregate adminSystemAggregate = adminSystemCommand.read();
        adminSystemAggregate.stopPostCollection();

        adminSystemCommand.save(adminSystemAggregate);
        postCollectionEnableLoader.toDisable();
    }

    public void startPostCollectionCycle(int batchSize) {
        if (postCollectionEnableLoader.isEnabled()) {
            throw new IllegalStateException("게시글 수집 기능이 이미 실행 중 입니다.");
        }
        postCollectSystem.collectPosts(batchSize);
    }

    public void changePostCollectionDelay(int delay) {
        AdminSystemAggregate adminSystemAggregate = adminSystemCommand.read();
        adminSystemAggregate.changePostCollectionDelay(delay);

        adminSystemCommand.save(adminSystemAggregate);
        postCollectScheduledService.changePostCollectionDelay(delay);
    }

}