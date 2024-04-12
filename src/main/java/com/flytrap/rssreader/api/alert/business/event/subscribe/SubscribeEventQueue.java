//package com.flytrap.rssreader.api.alert.business.event.subscribe;
//
//import java.util.Queue;
//import java.util.concurrent.LinkedBlockingQueue;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class SubscribeEventQueue {
//
//    private final Queue<SubscribeEvent> queue;
//    private final int queueSize;
//
//    private SubscribeEventQueue(int size) {
//        this.queueSize = size;
//        this.queue = new LinkedBlockingQueue<>(queueSize);
//    }
//
//    public static SubscribeEventQueue of(int size) {
//        return new SubscribeEventQueue(size);
//    }
//
//    public boolean offer(SubscribeEvent event) {
//        return queue.offer(event);
//    }
//
//    public SubscribeEvent peek() {
//        return queue.peek();
//    }
//
//
//    public SubscribeEvent poll() {
//        if (queue.isEmpty()) {
//            throw new IllegalStateException("No events in the queue !");
//        }
//        return queue.poll();
//    }
//
//    private int size() {
//        return queue.size();
//    }
//
//    public boolean isFull() {
//        return size() == queueSize;
//    }
//
//    public boolean isRemaining() {
//        return size() > 0;
//    }
//
//}
