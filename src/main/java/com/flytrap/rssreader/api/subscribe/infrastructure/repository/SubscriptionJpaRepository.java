package com.flytrap.rssreader.api.subscribe.infrastructure.repository;

import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionJpaRepository extends
        JpaRepository<SubscriptionEntity, Long> {

}
