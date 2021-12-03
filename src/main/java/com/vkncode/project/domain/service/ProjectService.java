package com.vkncode.project.domain.service;

import com.vkncode.project.config.RabbitConfig;
import com.vkncode.project.domain.dto.ProjectDTO;
import com.vkncode.project.domain.entity.Project;
import com.vkncode.project.domain.entity.State;
import com.vkncode.project.domain.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "projects")
public class ProjectService {

    private ProjectRepository repository;
    private RabbitTemplate rabbitTemplate;

    @Cacheable(unless = "#result == null")
    public List<Project> get() {
        return repository.findAll();
    }

    public Project findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException(id + " not available"));
    }

    @CacheEvict(allEntries = true)
    public Project saveNew(ProjectDTO projectDTO) {
        Project project = new Project(projectDTO);
        project.setState(State.SENT_TO_VALIDATION);
        project = repository.save(project);

        this.rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY_PROJECT_TO_SECRETARIAT,
                project);
        return project;
    }

    @CacheEvict(allEntries = true)
    public Project update(ProjectDTO projectDTO) {
        Project project = new Project(projectDTO);
        return repository.save(project);
    }

    @CacheEvict(allEntries = true)
    public Project update(Project project) {
        return repository.save(project);
    }

    @CacheEvict(allEntries = true)
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @CacheEvict(allEntries = true)
    public void reprocess(Long id) {
        Project project = repository.findById(id).orElseThrow(() -> new RuntimeException(id + " not available"));

        project.setState(State.SENT_TO_VALIDATION);

        this.rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY_PROJECT_TO_SECRETARIAT,
                project);
    }
}
