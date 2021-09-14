package com.vkncode.project.service;

import com.vkncode.project.domain.dto.ProjectDTO;
import com.vkncode.project.domain.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProjectServiceTest {

    @Autowired
    private ProjectService service;

    @Test
    public void defaultPath() {
        ProjectDTO projectDTO = ProjectDTO.builder()
                .secretariat(1L)
                .build();

        service.saveNew(projectDTO);
    }
}
