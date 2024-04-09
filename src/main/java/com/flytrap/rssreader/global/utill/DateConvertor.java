package com.flytrap.rssreader.global.utill;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateConvertor {

    /*
     * 아래 변수들은 다양한 블로그 플랫폼에서 사용되는 여러 날짜 형식을 처리하기 위한 것입니다.
     * 아래 규칙들은 국제 표준인 ISO 8601을 따르는 날짜 및 시간 형식 패턴 문자열입니다.
     * 국제 표준이라 변할 경우가 없고, 코드 수정시 매번 찾아보기 번거러우므로 주석을 달아놓습니다.
     *
     * - "EEE"는 요일을 세 글자 약어로 표현합니다 (예: "Wed").
     * - "d"는 일자를 표현하며, 한 자리 일자는 0 없이 표현합니다 (예: "1"). 변수명은 SINGLE_DIGIT_DAY로 씁니다
     * - "dd"는 일자를 표현하며, 1-9일에는 앞에 0을 붙여 표현합니다 (예: "01"). 변수명은 DOUBLE_DIGIT_DAY로 씁니다
     * - "MMM"은 세 글자 약어로 된 월을 나타냅니다 (예: "Nov").
     * - "yyyy"는 네 자리 연도입니다.
     * - "HH:mm:ss"는 24시간 기준 시, 분, 초를 나타냅니다.
     * - "z"는 축약된 시간대 이름을 지정합니다 (예: "PST", "GMT") 변수명은 ABBREVIATED_TIMEZONE로 씁니다
     * - "Z"는 UTC로부터의 수치적 시간대 오프셋을 나타냅니다 (예: "+0900"). 변수명은 NUMERICAL_TIMEZONE로 씁니다.
     */
    private static final String SINGLE_DIGIT_DAY_ABBREVIATED_TIMEZONE = "EEE, d MMM yyyy HH:mm:ss z";
    private static final String DOUBLE_DIGIT_DAY_ABBREVIATED_TIMEZONE = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final String SINGLE_DIGIT_DAY_NUMERICAL_TIMEZONE = "EEE, d MMM yyyy HH:mm:ss Z";
    private static final String DOUBLE_DIGIT_DAY_NUMERICAL_TIMEZONE = "EEE, dd MMM yyyy HH:mm:ss Z";

    private static final DateTimeFormatter[] FORMATTERS = {
        DateTimeFormatter.ofPattern(SINGLE_DIGIT_DAY_ABBREVIATED_TIMEZONE, Locale.ENGLISH),
        DateTimeFormatter.ofPattern(DOUBLE_DIGIT_DAY_ABBREVIATED_TIMEZONE, Locale.ENGLISH),
        DateTimeFormatter.ofPattern(SINGLE_DIGIT_DAY_NUMERICAL_TIMEZONE, Locale.ENGLISH),
        DateTimeFormatter.ofPattern(DOUBLE_DIGIT_DAY_NUMERICAL_TIMEZONE, Locale.ENGLISH),
        DateTimeFormatter.ISO_DATE_TIME,
    };

    /**
     * "Wed, 29 Nov 2023 13:49:14 +0900" 및 "Tue, 07 Nov 2023 14:12:30 GMT"
     * 여러 형식의 날짜 문자열을 Instant 객체로 변환하는 메서드
     *
     * @param parsingDate Instant 객체로 변경할 날짜 문자열
     * @return Instant 객체
     */
    public static Instant convertToInstant(String parsingDate) {

        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return Instant.from(formatter.parse(parsingDate));
            } catch (DateTimeParseException ignored) {

            }
        }
        throw new IllegalStateException("Unexpected value: " + parsingDate);
    }

    /**
     * 블로그마다 pubDate 형태가 다 달라서 새로운 블로그 구독할때마다 테스트 해야 할듯합니다.
     * 테스트하기 쉽게 하기 위해 main메서드를 만들었습니다.
     * @param args
     */
    public static void main(String[] args) {
        String date = "Tue, 21 Nov 2023 07:30:08 +0000";
        Instant instant = convertToInstant(date);

        System.out.println(instant);
    }
}
