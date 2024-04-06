package com.flytrap.rssreader.fixture;

import com.flytrap.rssreader.api.subscribe.domain.Subscribe;

public class SubscribeFixtureFactory {

    public static Subscribe generateSubscribe() {
        return Subscribe.builder()
                .id(1L)
                .title("에이프의 블로그")
                .url("https://blog.naver.com/PostList.nhn?blogId=ape")
                .build();
    }

    public static Subscribe generateSubscribe(long id) {
        return Subscribe.builder()
                .id(id)
                .title("??의 블로그")
                .url("https://blog.naver.com/PostList.nhn?blogId=??")
                .build();
    }
}
