package com.vkncode.project.domain.service;

import com.vkncode.project.config.RabbitConfig;
import com.vkncode.project.domain.dto.ProjectDTO;
import com.vkncode.project.domain.entity.Project;
import com.vkncode.project.domain.entity.State;
import com.vkncode.project.domain.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {

    private ProjectRepository repository;
    private RabbitTemplate rabbitTemplate;

    public List<Project> get() {
        return repository.findAll();
    }

    public Project findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException(id + " not available"));
    }

    public Project saveNew(ProjectDTO projectDTO) {
        Project project = new Project(projectDTO);
        project.setState(State.SENT_TO_VALIDATION);
        project = repository.save(project);

        this.rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY_PROJECT_TO_SECRETARIAT,
                project);
        return project;
    }

    public Project update(ProjectDTO projectDTO) {
        Project project = new Project(projectDTO);
        return repository.save(project);
    }

    public Project update(Project project) {
        return repository.save(project);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void reprocess(Long id) {
        Project project = repository.findById(id).orElseThrow(() -> new RuntimeException(id + " not available"));

        project.setState(State.SENT_TO_VALIDATION);

        this.rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY_PROJECT_TO_SECRETARIAT,
                project);
    }
}
