package com.flytrap.rssreader.api.account.infrastructure.implement;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.account.domain.AccountName;
import com.flytrap.rssreader.api.account.infrastructure.entity.AccountEntity;
import com.flytrap.rssreader.api.account.infrastructure.repository.AccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountQuery {

    private final AccountJpaRepository accountJpaRepository;

    public Optional<Account> readByProviderKey(Long oauthPk) {
        return accountJpaRepository.findByProviderKey(oauthPk).map(AccountEntity::toReadOnly);
    }

    public List<Account> readAllByName(AccountName name) {
        return accountJpaRepository.findAllByName(name.value())
                .stream().map(AccountEntity::toReadOnly).toList();
    }

    public Optional<Account> readById(AccountId id) {
        return accountJpaRepository.findById(id.value()).map(AccountEntity::toReadOnly);
    }

    public List<Account> readAllById(Collection<AccountId> ids) {
        List<Long> accountIds =  ids.stream().map(AccountId::value).toList();
        return accountJpaRepository.findAllById(accountIds)
                .stream().map(AccountEntity::toReadOnly).toList();
    }
}
