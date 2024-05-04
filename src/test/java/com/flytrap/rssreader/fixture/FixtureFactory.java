package com.flytrap.rssreader.fixture;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.account.domain.AccountName;
import com.flytrap.rssreader.api.account.infrastructure.entity.AccountEntity;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.subscribe.domain.BlogPlatform;
import com.flytrap.rssreader.fixture.FixtureFields.MemberEntityFields;
import com.flytrap.rssreader.fixture.FixtureFields.MemberFields;
import com.flytrap.rssreader.fixture.FixtureFields.PostEntityFields;
import com.flytrap.rssreader.fixture.FixtureFields.RssItemResourceFields;
import com.flytrap.rssreader.fixture.FixtureFields.SubscribeEntityFields;
import com.flytrap.rssreader.fixture.FixtureFields.UserResourceFields;
import com.flytrap.rssreader.api.parser.dto.RssPostsData.RssItemData;
import com.flytrap.rssreader.api.auth.infrastructure.external.dto.UserResource;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import com.flytrap.rssreader.api.parser.dto.RssSubscribeData;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FixtureFactory {

    // Account
    public static UserResource generateUserResource() {
        return UserResource.builder()
                .id(UserResourceFields.id)
                .email(UserResourceFields.email)
                .login(UserResourceFields.login)
                .avatarUrl(UserResourceFields.avatarUrl)
                .build();
    }

    public static AccountEntity generateAccountEntity() {
        return AccountEntity.builder()
                .id(MemberEntityFields.id)
                .email(MemberEntityFields.email)
                .name(MemberEntityFields.name)
                .profile(MemberEntityFields.profile)
                .providerKey(MemberEntityFields.oauthPk)
                .authProvider(MemberEntityFields.authProvider)
                .build();
    }

    public static Account generateAccount() {
        return Account.builder()
                .id(new AccountId(MemberFields.id))
                .name(new AccountName(MemberFields.name))
                .email(MemberFields.email)
                .profile(MemberFields.profile)
                .providerKey(MemberFields.oauthPk)
                .authProvider(MemberFields.authProvider)
                .build();
    }

    public static Account generateAccount(Long id) {
        return Account.builder()
                .id(new AccountId(id))
                .name(new AccountName(MemberFields.name))
                .email(MemberFields.email)
                .profile(MemberFields.profile)
                .providerKey(MemberFields.oauthPk)
                .authProvider(MemberFields.authProvider)
                .build();
    }

    public static Account generateAnotherMember() {
        return Account.builder()
                .id(new AccountId(MemberFields.anotherId))
                .name(new AccountName(MemberFields.anotherName))
                .email(MemberFields.anotherEmail)
                .profile(MemberFields.anotherProfile)
                .providerKey(MemberFields.anotherOauthPk)
                .authProvider(MemberFields.anotherAuthProvider)
                .build();
    }

    // Post
    public static RssItemData generateRssItemData() {
        return new RssItemData(
                RssItemResourceFields.guid,
                RssItemResourceFields.title,
                RssItemResourceFields.description,
                RssItemResourceFields.pubDate,
                RssItemResourceFields.thumbnailUrl
        );
    }

    public static List<RssItemData> generate50RssItemDataList() {
        List<RssItemData> rssItemData = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            rssItemData.add(generateRssItemData());
        }
        return rssItemData;
    }

    public static PostEntity generatePostEntity(Long id) {
        return PostEntity.builder()
                .id(id)
                .guid(PostEntityFields.guid)
                .title(PostEntityFields.title)
                .description(PostEntityFields.description)
                .subscriptionId(PostEntityFields.subscribe.getId())
                .build();
    }

    public static List<PostEntity> generate100PostEntityList() {
        List<PostEntity> postEntities = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            postEntities.add(generatePostEntity((long) i));
        }

        return postEntities;
    }

    // Subscribe
    public static SubscribeEntity generateSubscribeEntity() {
        return SubscribeEntity.builder()
                .id(SubscribeEntityFields.id)
                .url(SubscribeEntityFields.url)
                .platform(SubscribeEntityFields.platform)
                .build();
    }

    public static SubscribeEntity generateSubscribeEntity(Long id) {
        return SubscribeEntity.builder()
            .id(id)
            .url(SubscribeEntityFields.url)
            .platform(SubscribeEntityFields.platform)
            .build();
    }

    public static List<SubscribeEntity> generateSubscribeEntityList(int times) {
        List<SubscribeEntity> subscribeEntities = new ArrayList<>();
        for (int i = 1; i <= times; i++) {
            subscribeEntities.add(generateSubscribeEntity((long) i));
        }

        return subscribeEntities;
    }

    public static Optional<RssSubscribeData> generateRssSubscribeData() {
        return Optional.of(new RssSubscribeData(
                RssItemResourceFields.title,
                //TODO: 깃허브, 티스토리도 추가하려면 테스트 코드를 바꿔야 할 듯 합니다.
                "https://v2.velog.io/rss/jinny-l",
                BlogPlatform.VELOG,
                RssItemResourceFields.description
        ));
    }
}
