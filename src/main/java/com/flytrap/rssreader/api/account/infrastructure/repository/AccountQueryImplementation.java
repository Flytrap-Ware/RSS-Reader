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
public class AccountQueryImplementation {

    private final AccountJpaRepository memberEntityJpaRepository;

    public Account save(Account account) {
        return memberEntityJpaRepository.save(AccountEntity.from(account)).toDomain();
    }

    public Optional<Account> readByProviderKey(Long oauthPk) {
        return memberEntityJpaRepository.findByProviderKey(oauthPk).map(AccountEntity::toDomain);
    }

    public List<Account> readAllByName(AccountName name) {
        return memberEntityJpaRepository.findAllByName(name.value())
                .stream().map(AccountEntity::toDomain).toList();
    }

    public Optional<Account> readById(AccountId id) {
        return memberEntityJpaRepository.findById(id.value()).map(AccountEntity::toDomain);
    }

    public List<Account> readAllById(Collection<AccountId> ids) {
        List<Long> accountIds =  ids.stream().map(AccountId::value).toList();
        return memberEntityJpaRepository.findAllById(accountIds)
                .stream().map(AccountEntity::toDomain).toList();
    }
}
