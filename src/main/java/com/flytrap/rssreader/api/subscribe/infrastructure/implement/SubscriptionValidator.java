package com.flytrap.rssreader.api.subscribe.infrastructure.implement;

import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.SubscriptionDslRepository;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.RssSourceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SubscriptionValidator {

    private final RssSourceJpaRepository rssSourceJpaRepository;
    private final SubscriptionDslRepository subscriptionDslRepository;

    @Transactional(readOnly = true)
    public boolean existsBy(FolderId folderId, String rssUrl) {
        return subscriptionDslRepository.existsByUrl(folderId.value(), rssUrl);
    }
}
