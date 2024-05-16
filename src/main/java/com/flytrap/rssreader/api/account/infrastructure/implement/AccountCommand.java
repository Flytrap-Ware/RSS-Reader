package com.flytrap.rssreader.api.account.infrastructure.implement;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.infrastructure.entity.AccountEntity;
import com.flytrap.rssreader.api.account.infrastructure.repository.AccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountCommand {

    private final AccountJpaRepository accountJpaRepository;

    public Account create(Account account) {
        return accountJpaRepository.save(AccountEntity.create(account)).toReadOnly();
    }
}
