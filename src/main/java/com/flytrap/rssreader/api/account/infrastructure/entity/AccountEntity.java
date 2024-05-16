package com.flytrap.rssreader.api.account.infrastructure.entity;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AccountRoll;
import com.flytrap.rssreader.api.account.domain.AuthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "account")
@EntityListeners(AuditingEntityListener.class)
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String email;

    @Column(length = 2500, nullable = false)
    private String profile;

    @Column(name = "oauth_pk", unique = true, nullable = false)
    private Long providerKey;

    @Column(name = "oauth_server", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column(name = "roll", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountRoll roll;

    @CreatedDate
    private Instant createdAt;

    @Builder
    protected AccountEntity(Long id, String name, String email, String profile, Long providerKey,
                            AuthProvider authProvider, AccountRoll roll) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.providerKey = providerKey;
        this.authProvider = authProvider;
        this.roll = roll;
    }

    public Account toReadOnly() {
        return Account.of(
            this.id, this.name, this.email, this.profile, this.providerKey,
            this.authProvider, this.roll, this.createdAt
        );
    }

    public static AccountEntity create(Account account) {
        return AccountEntity.builder()
            .name(account.getName().value())
            .email(account.getEmail())
            .profile(account.getProfile())
            .providerKey(account.getProviderKey())
            .authProvider(account.getAuthProvider())
            .roll(account.getRoll())
            .build();
    }
}
