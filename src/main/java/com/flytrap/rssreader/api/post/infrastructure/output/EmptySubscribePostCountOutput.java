package com.flytrap.rssreader.api.post.infrastructure.output;

public class EmptySubscribePostCountOutput implements PostSubscribeCountOutput {

    @Override
    public long getSubscriptionId() {
        return 0;
    }

    @Override
    public int getPostCount() {
        return 0;
    }
}
