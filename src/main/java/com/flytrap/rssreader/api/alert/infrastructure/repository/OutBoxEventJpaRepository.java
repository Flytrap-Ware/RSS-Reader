package com.flytrap.rssreader.api.alert.infrastructure.repository;

import com.flytrap.rssreader.api.alert.infrastructure.entity.OutBoxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OutBoxEventJpaRepository extends JpaRepository<OutBoxEventEntity, Long> {

    void deleteByIdIn(List<Long> list);
}
