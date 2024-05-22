package com.flytrap.rssreader.api.post.domain;


import com.flytrap.rssreader.api.subscribe.domain.BlogPlatform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class PostBlogPlatformData {
    private BlogPlatform platform;
    private Long postCount;
}
