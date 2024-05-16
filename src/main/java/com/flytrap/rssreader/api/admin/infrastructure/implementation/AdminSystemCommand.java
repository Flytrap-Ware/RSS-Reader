package com.flytrap.rssreader.api.admin.infrastructure.implementation;

import com.flytrap.rssreader.api.admin.domain.AdminSystemAggregate;
import com.flytrap.rssreader.api.admin.infrastructure.entity.AdminSystemEntity;
import com.flytrap.rssreader.api.admin.infrastructure.repository.AdminSystemJpaRepository;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSystemCommand {

    private final AdminSystemJpaRepository adminSystemJpaRepository;

    public AdminSystemAggregate read() {
        return adminSystemJpaRepository.findById(1L)
            .orElseThrow(() -> new NoSuchDomainException(AdminSystemAggregate.class))
            .toAggregate();
    }

    public AdminSystemAggregate save(AdminSystemAggregate adminSystemAggregate) {
        return adminSystemJpaRepository.save(AdminSystemEntity.from(adminSystemAggregate))
            .toAggregate();
    }
}
