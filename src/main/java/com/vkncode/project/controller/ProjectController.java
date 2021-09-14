package com.vkncode.project.controller;

import com.vkncode.project.domain.dto.ProjectDTO;
import com.vkncode.project.domain.entity.Project;
import com.vkncode.project.domain.service.ProjectService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
@Api(value = "Project", tags = { "Project" })
public class ProjectController {

    private ProjectService service;

    @GetMapping
    public List<Project> getList() {
        return service.get();
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiParam(
            name =  "id",
            type = "Long",
            value = "Project id",
            example = "2",
            required = true)
    @GetMapping("/{id}")
    public Project getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add Project",
            notes = "This method adds a new Project")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Project added"),
            @ApiResponse(code = 500, message = "Internal Error"),
    })
    @PostMapping
    public Project create(@Valid @RequestBody ProjectDTO projectDTO) {
        return service.saveNew(projectDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update Project",
            notes = "This method updated a project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Project updated"),
            @ApiResponse(code = 500, message = "Internal Error"),
    })

    @PutMapping("/{id}")
    public Project update(@Valid @RequestBody ProjectDTO projectDTO, @PathVariable Long id) {
        projectDTO.setId(id);
        return service.update(projectDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/{id}/reprocess")
    public void reprocess(@PathVariable Long id) {
        service.reprocess(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Remove project",
            notes = "This method removes a Project")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Project updated"),
            @ApiResponse(code = 500, message = "Internal Error"),
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
