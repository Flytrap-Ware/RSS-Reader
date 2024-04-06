package com.flytrap.rssreader.api.member.domain;

import com.flytrap.rssreader.global.model.DefaultDomain;
import com.flytrap.rssreader.global.model.Domain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Domain(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements DefaultDomain {

    private Long id;
    private String name;
    private String email;
    private String profile;
    private OauthInfo oauthInfo;
    private Instant createdAt;

    @Builder
    private Member(Long id, String name, String email, String profile, long oauthPk,
            OauthServer oauthServer,
            Instant createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.oauthInfo = new OauthInfo(oauthPk, oauthServer);
        this.createdAt = createdAt;
    }

    public static Member of(Long id, String name, String email, String profile, Long oauthPk,
                            OauthServer oauthServer, Instant createdAt) {
        return Member.builder()
                .id(id)
                .name(name)
                .email(email)
                .profile(profile)
                .oauthPk(oauthPk)
                .oauthServer(oauthServer)
                .createdAt(createdAt)
                .build();
    }

    public static Member adminOf(long userId, String userName, String userEmail, String profile) {
        return Member.builder()
                .id(userId)
                .name(userName)
                .email(userEmail)
                .profile(profile)
                .build();
    }

    public Long getOauthPk() {
        return this.oauthInfo.getOauthPk();
    }

    public OauthServer getOauthServer() {
        return this.oauthInfo.getOauthServer();
    }
}
