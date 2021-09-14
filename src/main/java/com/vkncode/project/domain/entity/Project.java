package com.vkncode.project.domain.entity;

import com.vkncode.project.domain.dto.ProjectDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long secretariat;

    private double cost;

    private String title;

    private String description;

    private Destination destination;

    private State state;

    public Project(ProjectDTO dto) {
        this.id = dto.getId();
        this.secretariat = dto.getSecretariat();
        this.cost = dto.getCost();
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.destination = dto.getDestination();
    }
}
