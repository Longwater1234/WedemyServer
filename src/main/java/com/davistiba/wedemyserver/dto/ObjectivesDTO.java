package com.davistiba.wedemyserver.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Getter
@RequiredArgsConstructor
public class ObjectivesDTO {
    @NotNull
    private Integer courseId;
    @NotEmpty
    private ArrayList<String> objectives;


}
