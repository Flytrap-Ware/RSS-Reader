package com.flytrap.rssreader.api.subscribe.infrastructure.implement;

import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.parser.RssSubscribeParser;
import com.flytrap.rssreader.api.parser.dto.RssSubscribeData;
import com.flytrap.rssreader.api.post.business.event.PostCollectEvent;
import com.flytrap.rssreader.api.subscribe.domain.FolderSubscription;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.FolderSubscribeEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.FolderSubscriptionEntityJpaRepository;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.SubscribeEntityJpaRepository;
import com.flytrap.rssreader.global.event.GlobalEventPublisher;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FolderSubscriptionCommand {

    private final FolderSubscriptionEntityJpaRepository folderSubscriptionEntityJpaRepository;
    private final SubscribeEntityJpaRepository subscribeEntityJpaRepository;
    private final RssSubscribeParser rssSubscribeParser;
    private final GlobalEventPublisher globalEventPublisher;

    @Transactional
    public FolderSubscription createFrom(FolderId folderId, String rssUrl) {
        Optional<SubscribeEntity> rssSourceEntityOptional = subscribeEntityJpaRepository
            .findByUrl(rssUrl);

        if (rssSourceEntityOptional.isPresent()) {
            SubscribeEntity rssSourceEntity = rssSourceEntityOptional.get();
            FolderSubscribeEntity newFolderSubscribeEntity = FolderSubscribeEntity.builder()
                .folderId(folderId.value())
                .subscribeId(rssSourceEntity.getId())
                .description("") // TODO: 컬럼 제거하기
                .build();

            return folderSubscriptionEntityJpaRepository.save(newFolderSubscribeEntity)
                .toReadOnly(rssSourceEntity);
        } else {
            RssSubscribeData rssSubscribeData = rssSubscribeParser.parseRssDocuments(rssUrl)
                .orElseThrow();
            SubscribeEntity newRssSourceEntity = subscribeEntityJpaRepository
                .save(SubscribeEntity.from(rssSubscribeData));
            FolderSubscribeEntity newFolderSubscribeEntity = FolderSubscribeEntity.builder()
                .folderId(folderId.value())
                .subscribeId(newRssSourceEntity.getId())
                .description("") // TODO: 컬럼 제거하기
                .build();

            globalEventPublisher.publish(new PostCollectEvent(newRssSourceEntity));

            return folderSubscriptionEntityJpaRepository.save(newFolderSubscribeEntity)
                .toReadOnly(newRssSourceEntity);
        }
    }

}
