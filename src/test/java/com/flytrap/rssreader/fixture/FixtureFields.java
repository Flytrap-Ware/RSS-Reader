package com.flytrap.rssreader.fixture;

import com.flytrap.rssreader.api.account.domain.AuthProvider;
import com.flytrap.rssreader.api.subscribe.domain.BlogPlatform;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import java.time.Instant;

public class FixtureFields {

    private static final Long LONG_1L = 1L;
    private static final String EMAIL_TEST_GMAIL = "test@gmail.com";
    private static final String LOGIN = "login";
    private static final String NAME = "name";
    private static final String AVATAR_URL = "https://avatarUrl.jpg";
    private static final String RSS_URL = "https://avatarUrl.jpg";
    private static final String GUID = "guid";
    private static final Instant INSTANT_20231115 = Instant.parse("2023-11-15T00:00:00Z");
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    public static class UserResourceFields {

        public static Long id = LONG_1L;
        public static String email = EMAIL_TEST_GMAIL;
        public static String login = LOGIN;
        public static String avatarUrl = AVATAR_URL;
    }

    public static class MemberEntityFields {

        public static Long id = LONG_1L;
        public static String email = EMAIL_TEST_GMAIL;
        public static String name = NAME;
        public static String profile = AVATAR_URL;
        public static Long oauthPk = LONG_1L;
        public static AuthProvider authProvider = AuthProvider.GITHUB;
    }

    public static class MemberFields {

        public static Long id = LONG_1L;
        public static String name = NAME;
        public static String email = EMAIL_TEST_GMAIL;
        public static String profile = AVATAR_URL;
        public static Long oauthPk = LONG_1L;
        public static AuthProvider authProvider = AuthProvider.GITHUB;
        public static Long anotherId = 2L;
        public static String anotherName = "anotherName";
        public static String anotherEmail = "anotherEmail";
        public static String anotherProfile = "anotherProfile";
        public static long anotherOauthPk = 2L;
        public static AuthProvider anotherAuthProvider = AuthProvider.GITHUB;
    }

    public static class RssItemResourceFields {

        public static String guid = GUID;
        public static String title = TITLE;
        public static String description = DESCRIPTION;
        public static Instant pubDate = INSTANT_20231115;
        public static String thumbnailUrl = AVATAR_URL;
    }

    public static class SubscribeEntityFields {

        public static Long id = LONG_1L;
        public static String url = RSS_URL;
        public static String description = DESCRIPTION;
        public static BlogPlatform platform = BlogPlatform.VELOG;
    }

    public static class PostEntityFields {

        public static Long id = LONG_1L;
        public static String guid = GUID;
        public static String title = TITLE;
        public static String description = DESCRIPTION;
        public static SubscribeEntity subscribe = FixtureFactory.generateSubscribeEntity();
    }
}
