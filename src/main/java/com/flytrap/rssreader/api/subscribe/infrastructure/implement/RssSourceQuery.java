package com.flytrap.rssreader.api.subscribe.infrastructure.implement;

import com.flytrap.rssreader.api.subscribe.domain.RssSource;
import com.flytrap.rssreader.api.subscribe.domain.RssSourceId;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.RssResourceJpaRepository;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RssSourceQuery {

    private final RssResourceJpaRepository rssResourceJpaRepository; // TODO: Subscription으로 변경하기

    public RssSource read(RssSourceId rssSourceId) {
        return rssResourceJpaRepository.findById(rssSourceId.value())
            .orElseThrow(() -> new NoSuchDomainException(RssSource.class))
            .toExistingRssSource();
    }

}
