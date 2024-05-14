package com.flytrap.rssreader.api.subscribe.infrastructure.repository;

import com.flytrap.rssreader.api.subscribe.infrastructure.entity.RssSourceEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RssResourceJpaRepository extends JpaRepository<RssSourceEntity, Long> {

    boolean existsByUrl(String url);

    Optional<RssSourceEntity> findByUrl(String blogUrl);

}
