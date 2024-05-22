package com.flytrap.rssreader.api.admin.presentation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCollectionCycleRequest {

    @Min(value = 1, message = "batchSize는 음수 값은 허용되지 않습니다.")
    @Max(value = 100_000, message = "batchSize는 100,000을 초과할 수 없습니다.")
    private int batchSize = 1;
}
