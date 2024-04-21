package com.flytrap.rssreader.api.account.infrastructure.repository;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.account.domain.AccountName;
import com.flytrap.rssreader.api.account.infrastructure.entity.AccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountImplementation {

    private final AccountEntityJpaRepository memberEntityJpaRepository;

    public AccountEntity save(Account account) {
        return memberEntityJpaRepository.save(AccountEntity.from(account));
    }

    public Optional<AccountEntity> findByProviderKey(Long oauthPk) {
        return memberEntityJpaRepository.findByProviderKey(oauthPk);
    }

    public List<AccountEntity> findAllByName(AccountName name) {
        return memberEntityJpaRepository.findAllByName(name.value());
    }

    public Optional<AccountEntity> findById(AccountId id) {
        return memberEntityJpaRepository.findById(id.value());
    }

    public List<AccountEntity> findAllById(Collection<AccountId> ids) {
        List<Long> accountIds =  ids.stream().map(AccountId::value).toList();
        return memberEntityJpaRepository.findAllById(accountIds);
    }
}
