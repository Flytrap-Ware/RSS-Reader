package com.flytrap.rssreader.api.admin.presentation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCollectionCoreSizeRequest {

    @Min(value = 1, message = "coreSize는 0보다 커야 합니다.")
    @Max(value = 500 , message = "coreSize는 500을 초과할 수 없습니다.")
    private int coreSize = 1;
}
