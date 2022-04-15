package com.davistiba.wedemyserver.dto;

import com.davistiba.wedemyserver.models.SummaryTitle;
import lombok.Value;

import java.io.Serializable;

@Value
public class UserSummary implements Serializable {
    private static final long serialVersionUID = -6556685957514582951L;
    SummaryTitle title;
    Long value;
    String subtitle;
}
