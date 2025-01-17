package com.flytrap.rssreader.api.account.business.service;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.account.domain.AccountName;
import com.flytrap.rssreader.api.account.infrastructure.implement.AccountCommand;
import com.flytrap.rssreader.api.account.infrastructure.implement.AccountQuery;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import com.flytrap.rssreader.api.auth.infrastructure.external.dto.UserResource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountQuery accountQuery;
    private final AccountCommand accountCommand;

    /**
     * Member 정보를 반환합니다.
     * User resource가 Member 테이블에 존재하지 않으면 새로 가입 후 Member 정보를 반환합니다.
     * @param userResource
     * @return Account domain
     */
    @Transactional
    public Account login(UserResource userResource) {
        return accountQuery.readByProviderKey(userResource.getId())
                .orElseGet(() -> signUp(userResource.newAccount()));
    }

    /**
     * Account 새로운 회원을 가입시킵니다.
     * @param account
     * @return saved account
     */
    private Account signUp(Account account) {
        return accountCommand.create(account);
    }

    /**
     * 이름으로 회원을 검색합니다.
     * @param name
     * @return Account domain list
     */
    public List<Account> get(AccountName name) {
        return accountQuery.readAllByName(name).stream()
                .toList();
    }

    /**
     * id로 회원을 검색합니다.
     * @param id Account id
     * @return Account domain
     * @throws NoSuchDomainException
     */
    public Account get(AccountId id) {
        return accountQuery.readById(id)
                .orElseThrow(() -> new NoSuchDomainException(Account.class));
    }

    /**
     * id 목록으로 회원을 검색합니다.
     * @param ids list
     * @return Account domain list
     */
    public List<Account> getAll(Collection<AccountId> ids) {
        return accountQuery.readAllById(ids).stream()
                .toList();
    }
}
