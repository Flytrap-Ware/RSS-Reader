package com.flytrap.rssreader.api.subscribe.business.service;

import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import com.flytrap.rssreader.api.parser.RssSubscribeParser;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.SubscribeEntityJpaRepository;
import com.flytrap.rssreader.api.parser.dto.RssSubscribeData;
import com.flytrap.rssreader.api.subscribe.presentation.dto.SubscribeRequest.CreateRequest;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
@Service
public class SubscribeService {

    private final SubscribeEntityJpaRepository subscribeRepository;
    private final RssSubscribeParser rssSubscribeParser;

    @Transactional
    public Subscribe subscribe(CreateRequest request) {
        RssSubscribeData rssSubscribeData = rssSubscribeParser.parseRssDocuments(request).orElseThrow();

        if (!subscribeRepository.existsByUrl(request.blogUrl())) {
            return subscribeRepository.save(SubscribeEntity.from(rssSubscribeData))
                .toNewSubscribeDomain();
        }

        return subscribeRepository.findByUrl(request.blogUrl()).orElseThrow()
            .toExistingSubscribeDomain();
    }

    public List<Subscribe> read(Collection<Long> subscribeIds) {
        return subscribeRepository.findAllById(subscribeIds).stream()
                .map(SubscribeEntity::toExistingSubscribeDomain)
                .toList();
    }

}
