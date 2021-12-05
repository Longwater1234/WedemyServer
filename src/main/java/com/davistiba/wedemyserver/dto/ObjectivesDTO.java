package com.davistiba.wedemyserver.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
public class ObjectivesDTO {
    @NotNull
    private Integer courseId;
    @NotEmpty
    private ArrayList<String> objectives;


}
