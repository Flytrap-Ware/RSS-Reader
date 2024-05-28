package com.flytrap.rssreader.api.subscribe.infrastructure.implement;

import com.flytrap.rssreader.api.subscribe.domain.RssSource;
import com.flytrap.rssreader.api.subscribe.domain.RssSourceId;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.RssSourceEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.RssResourceJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RssSourceQuery {

    private final RssResourceJpaRepository rssResourceJpaRepository;

    public Optional<RssSource> read(RssSourceId rssSourceId) {
        return rssResourceJpaRepository.findById(rssSourceId.value())
            .map(RssSourceEntity::toExistingRssSource);
    }

}
