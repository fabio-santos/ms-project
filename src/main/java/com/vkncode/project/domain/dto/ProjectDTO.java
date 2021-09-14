package com.vkncode.project.domain.dto;

import com.vkncode.project.domain.entity.Destination;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class ProjectDTO {

    private Long id;

    private Long secretariat;

    private double cost;

    private String title;

    private String description;

    private Destination destination;
}
