package com.flytrap.rssreader.fixture;

import com.flytrap.rssreader.api.member.domain.Member;
import com.flytrap.rssreader.api.member.infrastructure.entity.MemberEntity;
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

    // Member
    public static UserResource generateUserResource() {
        return UserResource.builder()
                .id(UserResourceFields.id)
                .email(UserResourceFields.email)
                .login(UserResourceFields.login)
                .avatarUrl(UserResourceFields.avatarUrl)
                .build();
    }

    public static MemberEntity generateMemberEntity() {
        return MemberEntity.builder()
                .id(MemberEntityFields.id)
                .email(MemberEntityFields.email)
                .name(MemberEntityFields.name)
                .profile(MemberEntityFields.profile)
                .oauthPk(MemberEntityFields.oauthPk)
                .oauthServer(MemberEntityFields.oauthServer)
                .build();
    }

    public static Member generateMember() {
        return Member.builder()
                .id(MemberFields.id)
                .name(MemberFields.name)
                .email(MemberFields.email)
                .profile(MemberFields.profile)
                .oauthPk(MemberFields.oauthPk)
                .oauthServer(MemberFields.oauthServer)
                .build();
    }

    public static Member generateMember(Long id) {
        return Member.builder()
                .id(id)
                .name(MemberFields.name)
                .email(MemberFields.email)
                .profile(MemberFields.profile)
                .oauthPk(MemberFields.oauthPk)
                .oauthServer(MemberFields.oauthServer)
                .build();
    }

    public static Member generateAnotherMember() {
        return Member.builder()
                .id(MemberFields.anotherId)
                .name(MemberFields.anotherName)
                .email(MemberFields.anotherEmail)
                .profile(MemberFields.anotherProfile)
                .oauthPk(MemberFields.anotherOauthPk)
                .oauthServer(MemberFields.anotherOauthServer)
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
                .subscribe(PostEntityFields.subscribe)
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
