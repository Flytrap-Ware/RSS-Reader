package com.flytrap.rssreader.api.subscribe.infrastructure.repository;

import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeEntityJpaRepository extends JpaRepository<SubscribeEntity, Long> {

    boolean existsByUrl(String url);

    Optional<SubscribeEntity> findByUrl(String blogUrl);

}
