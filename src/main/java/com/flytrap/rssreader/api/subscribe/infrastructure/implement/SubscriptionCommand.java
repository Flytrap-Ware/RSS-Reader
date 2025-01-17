package com.flytrap.rssreader.api.subscribe.infrastructure.implement;

import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.parser.RssSubscribeParser;
import com.flytrap.rssreader.api.parser.dto.RssSourceData;
import com.flytrap.rssreader.api.post.business.event.PostCollectEvent;
import com.flytrap.rssreader.api.subscribe.domain.Subscription;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscriptionEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.RssSourceEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.SubscriptionJpaRepository;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.RssSourceJpaRepository;
import com.flytrap.rssreader.global.event.GlobalEventPublisher;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SubscriptionCommand {

    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final RssSourceJpaRepository rssSourceJpaRepository;
    private final RssSubscribeParser rssSubscribeParser;
    private final GlobalEventPublisher globalEventPublisher;

    @Transactional
    public Subscription createFrom(FolderId folderId, String rssUrl) {
        Optional<RssSourceEntity> rssSourceEntityOptional = rssSourceJpaRepository
            .findByUrl(rssUrl);

        if (rssSourceEntityOptional.isPresent()) {
            RssSourceEntity rssSourceEntity = rssSourceEntityOptional.get();
            SubscriptionEntity newSubscriptionEntity = SubscriptionEntity.builder()
                .folderId(folderId.value())
                .rssSourceId(rssSourceEntity.getId())
                .build();

            return subscriptionJpaRepository.save(newSubscriptionEntity)
                .toReadOnly(rssSourceEntity);
        } else {
            RssSourceData rssSourceData = rssSubscribeParser.parseRssDocuments(rssUrl)
                .orElseThrow();
            RssSourceEntity newRssSourceEntity = rssSourceJpaRepository
                .save(RssSourceEntity.from(rssSourceData));
            SubscriptionEntity newSubscriptionEntity = SubscriptionEntity.builder()
                .folderId(folderId.value())
                .rssSourceId(newRssSourceEntity.getId())
                .build();

            globalEventPublisher.publish(new PostCollectEvent(newRssSourceEntity));

            return subscriptionJpaRepository.save(newSubscriptionEntity)
                .toReadOnly(newRssSourceEntity);
        }
    }

    public void delete(SubscriptionId subscriptionId) {
        subscriptionJpaRepository.deleteById(subscriptionId.value());
    }

}
