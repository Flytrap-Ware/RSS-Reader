package com.flytrap.rssreader.api.account.infrastructure.repository;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.infrastructure.entity.AccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountCommand {

    private final AccountJpaRepository memberEntityJpaRepository;

    public Account create(Account account) {
        return memberEntityJpaRepository.save(AccountEntity.from(account)).toDomain();
    }
}
