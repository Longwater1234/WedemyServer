package com.davistiba.wedemyserver.dto;

import com.davistiba.wedemyserver.models.SummaryTitle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserSummary {
    private SummaryTitle title;
    private Long value;
    private String subtitle = "courses";
}
