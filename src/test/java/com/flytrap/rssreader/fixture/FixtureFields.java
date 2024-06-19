package com.flytrap.rssreader.fixture;

import com.flytrap.rssreader.api.account.domain.AuthProvider;

public class FixtureFields {

    private static final Long LONG_1L = 1L;
    private static final String EMAIL_TEST_GMAIL = "test@gmail.com";
    private static final String LOGIN = "login";
    private static final String NAME = "name";
    private static final String AVATAR_URL = "https://avatarUrl.jpg";

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

}
