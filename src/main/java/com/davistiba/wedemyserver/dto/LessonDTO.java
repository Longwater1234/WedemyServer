package com.davistiba.wedemyserver.dto;

/**
 * Used for complex native SQL query, see LessonRepository
 */
public interface LessonDTO {
    String getId();

    String getLessonName();

    Integer getPosition();

    // MySQL uses int for Boolean
    Integer getIsWatched();

    // text format "mm:ss"
    String getVideoTime();
}
