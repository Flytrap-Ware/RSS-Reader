//package com.flytrap.rssreader.api.alert.business.event.subscribe;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class SubscribeEventListener {
//
//    private final SubscribeEventQueue eventQueue;
//
//    @EventListener
//    public void onEvent(SubscribeEvent event) {
//        if (eventQueue.isFull()) {
//            log.info("eventQueue full ");
//            return;
//        }
//        eventQueue.offer(event);
//    }
//}
