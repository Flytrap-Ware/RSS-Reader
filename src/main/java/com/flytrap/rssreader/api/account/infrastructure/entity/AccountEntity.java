package com.flytrap.rssreader.api.account.infrastructure.entity;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AuthProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

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

    @CreatedDate
    private Instant createdAt;

    @Builder
    protected AccountEntity(Long id, String name, String email, String profile, Long providerKey,
                            AuthProvider authProvider) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.providerKey = providerKey;
        this.authProvider = authProvider;
    }

    public Account toDomain() {
        return Account.of(this.id, this.name, this.email, this.profile, this.providerKey, this.authProvider, this.createdAt);
    }

    public static AccountEntity from(Account account) {
        return AccountEntity.builder()
            .id(account.getId().value())
            .name(account.getName().value())
            .email(account.getEmail())
            .profile(account.getProfile())
            .providerKey(account.getProviderKey())
            .authProvider(account.getAuthProvider())
            .build();
    }
}
