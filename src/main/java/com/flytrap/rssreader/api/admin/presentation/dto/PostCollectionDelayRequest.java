package com.flytrap.rssreader.api.admin.presentation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCollectionDelayRequest {

    @Min(value = 1, message = "delay는 0보다 커야 합니다.")
    @Max(value = 86_400_000 , message = "delay는 86_400_000을 초과할 수 없습니다.")
    private int delay = 1;
}
