package com.flytrap.rssreader.global.utill;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateConvertorTest {

    @Test
    @DisplayName("다양한 offset을 가진 pubDate를 Instant로 변환할 수 있다.")
    void convertToInstant() {
        String[] pubDates = {
            "Tue, 21 Nov 2023 14:12:30 +0900",
            "Tue, 21 Nov 2023 14:12:30 +0000",
            "Tue, 7 Nov 2023 14:12:30 +0900",
            "Tue, 7 Nov 2023 14:12:30 +0000",
            "Tue, 07 Nov 2023 14:12:30 GMT",
            "Tue, 7 Nov 2023 14:12:30 GMT"
        };

        // 에러가 나지 않으면 Instant로 변환에 성공한 것
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThatCode(() -> {
            for (String pubDate : pubDates) {
                DateConvertor.convertToInstant(pubDate);
            }
        }).doesNotThrowAnyException();
        softAssertions.assertAll();
    }

    @Test
    void 잘못된_날짜_형식을_변환하려_하면_예외를_던진다() {
        String illegalPubDate = "Illegal Publish Date";

        Assertions.assertThrows(
            IllegalStateException.class,
            () -> DateConvertor.convertToInstant(illegalPubDate)
        );
    }

}