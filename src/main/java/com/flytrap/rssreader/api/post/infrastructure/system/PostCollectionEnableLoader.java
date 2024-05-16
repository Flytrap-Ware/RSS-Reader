package com.flytrap.rssreader.api.post.infrastructure.system;

import com.flytrap.rssreader.api.admin.domain.AdminSystemAggregate;
import com.flytrap.rssreader.api.admin.infrastructure.repository.AdminSystemJpaRepository;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCollectionEnableLoader implements CommandLineRunner {

    private boolean isEnable;

    private final AdminSystemJpaRepository adminSystemJpaRepository;

    @Override
    public void run(String... args) throws Exception {
        this.isEnable = adminSystemJpaRepository.findById(1L)
            .orElseThrow(() -> new NoSuchDomainException(AdminSystemAggregate.class))
            .isPostCollectionEnabled();
    }

    public boolean isDisabled() {
        return !this.isEnable;
    }

    public void toEnable() {
        this.isEnable = true;
    }

    public void toDisable() {
        this.isEnable = false;
    }
}
