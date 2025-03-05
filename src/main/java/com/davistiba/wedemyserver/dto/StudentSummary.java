package com.davistiba.wedemyserver.dto;

import com.davistiba.wedemyserver.models.SummaryTitle;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public class StudentSummary implements Serializable {
    @Serial
    private static final long serialVersionUID = -6556685957514582951L;

    private SummaryTitle title;
    private Long value;
    private String subtitle;
}
