package com.flytrap.rssreader.api.post.business.event.postInsert;


import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class PostBulkInsertQueue {

    private final Queue<PostEntity> queue;
    private final int space;

    private PostBulkInsertQueue(int size) {
        this.space = size;
        this.queue = new LinkedBlockingQueue<>(space);
    }

    public static PostBulkInsertQueue of(int size) {
        return new PostBulkInsertQueue(size);
    }

    public void offer(PostEntity event) {
        boolean returnValue = queue.offer(event);
    }

    public PostEntity peek() {
        return queue.peek();
    }


    public PostEntity poll() {
        if (queue.isEmpty()) {
            throw new IllegalStateException("No events in the queue !");
        }
        PostEntity event = queue.poll();
        return event;
    }

    public int size() {
        return queue.size();
    }

    public boolean isFull() {
        return size() == space;
    }

    public boolean isRemaining() {
        return size() > 0;
    }

    public List<PostEntity> pollBatch(int batchSize) {
        List<PostEntity> postsToInsert = new ArrayList<>();
        for (int i = 0; i < batchSize && !queue.isEmpty(); i++) {
            postsToInsert.add(queue.poll());
        }
        return postsToInsert;
    }
}
