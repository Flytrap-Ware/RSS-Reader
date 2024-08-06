package com.flytrap.rssreader.api.subscribe.domain;

import java.time.Instant;
import java.time.Period;
import java.util.function.Predicate;

public enum RestrictionPolicy {
    NO_RESTRICTION_LEVEL(Period.ofDays(0), failCount -> failCount < 3),
    FIRST_RESTRICTION_LEVEL(Period.ofDays(1), failCount -> failCount == 3),
    SECOND_RESTRICTION_LEVEL(Period.ofDays(3), failCount -> failCount == 4),
    THIRD_RESTRICTION_LEVEL(Period.ofDays(7), failCount -> failCount == 5),
    FOURTH_RESTRICTION_LEVEL(Period.ofDays(30), failCount -> failCount >= 6 && failCount < 10),
    MAXIMUM_RESTRICTION_LEVEL(Period.ofDays(300 * 365), failCount -> failCount >= 10);

    private final Period restrictionPeriod;
    private final Predicate<Integer> condition;

    RestrictionPolicy(Period restrictionPeriod, Predicate<Integer> condition) {
        this.restrictionPeriod = restrictionPeriod;
        this.condition = condition;
    }

    public static Instant calculateRestrictionDate(int failCount, Instant referenceData) {
        for (RestrictionPolicy policy : RestrictionPolicy.values()) {
            if (policy.condition.test(failCount)) {
                return referenceData.plus(policy.restrictionPeriod);
            }
        }
        return referenceData;
    }
}
