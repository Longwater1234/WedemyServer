package com.davistiba.wedemyserver.dto;

import com.davistiba.wedemyserver.models.SummaryTitle;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class UserSummary implements Serializable {
    private static final long serialVersionUID = -6556685957514582951L;
    SummaryTitle title;
    Long value;
    String subtitle;
}
