package com.davistiba.wedemyserver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response body after updating watch status
 *
 * @since 2025-05-01
 */
@Getter
@Setter
@NoArgsConstructor
public class WatchStatusResp {

    private String nextLessonId;

    private String message = "";
}
