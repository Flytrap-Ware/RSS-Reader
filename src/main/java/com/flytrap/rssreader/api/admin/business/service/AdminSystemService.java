package com.flytrap.rssreader.api.admin.business.service;

import com.flytrap.rssreader.api.admin.domain.AdminSystemAggregate;
import com.flytrap.rssreader.api.admin.infrastructure.implementation.AdminSystemCommand;
import com.flytrap.rssreader.api.post.infrastructure.system.PostCollectionEnableLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminSystemService {

    private final AdminSystemCommand adminSystemCommand;
    private final PostCollectionEnableLoader postCollectionEnableLoader;

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

}
