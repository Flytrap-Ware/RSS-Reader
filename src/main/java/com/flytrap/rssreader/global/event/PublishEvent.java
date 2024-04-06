package com.flytrap.rssreader.global.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PublishEvent {
    Class<? extends EventHolder> eventType();
    String params(); // todo : 감자가 이벤트 구현할 때, 파라미터가 여러개 필요한 부분 있었음. 이부분 리팩토링 해보기

}
