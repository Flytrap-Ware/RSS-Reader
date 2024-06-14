package com.flytrap.rssreader.api.subscribe.infrastructure.implement;

import com.flytrap.rssreader.api.subscribe.domain.RssSource;
import com.flytrap.rssreader.api.subscribe.domain.RssSourceId;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.RssSourceEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.RssSourceJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RssSourceQuery {

    private final RssSourceJpaRepository rssSourceJpaRepository;

    public Optional<RssSource> read(RssSourceId rssSourceId) {
        return rssSourceJpaRepository.findById(rssSourceId.value())
            .map(RssSourceEntity::toExistingRssSource);
    }

}
