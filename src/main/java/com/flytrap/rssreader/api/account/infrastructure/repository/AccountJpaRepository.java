package com.flytrap.rssreader.api.account.infrastructure.repository;

import com.flytrap.rssreader.api.account.infrastructure.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByProviderKey(long oauthPk);
    List<AccountEntity> findAllByName(String name);
}
