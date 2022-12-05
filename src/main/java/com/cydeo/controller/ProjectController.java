package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.ProjectService;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vi/project")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getProjects(){
       List<ProjectDTO> listOfProjects= projectService.listAllProjects();
        return ResponseEntity.ok(new ResponseWrapper("project list", listOfProjects, HttpStatus.OK));

    }
    @GetMapping("/{projectCode}")
    public ResponseEntity<ResponseWrapper> getProjectByCode(@PathVariable("projectCode") String projectCode){

       ProjectDTO project= projectService.getByProjectCode(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("project found", project, HttpStatus.OK));

    }
    @PostMapping
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO){
        projectService.save(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("project created",  HttpStatus.CREATED));
    }
    @PutMapping
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO){
        projectService.update(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("project updated",  HttpStatus.OK));
    }
    @DeleteMapping("/{code}")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("code") String code){
        projectService.delete(code);
        return ResponseEntity.ok(new ResponseWrapper("project deleted",  HttpStatus.OK));
    }
    @GetMapping("/manager/project-status}")
    public ResponseEntity<ResponseWrapper> getProjectByManager(@RequestBody UserDTO manager){

        List<ProjectDTO> listOfProjects=projectService.listAllProjectDetails();
        return ResponseEntity.ok(new ResponseWrapper("projects by manager",listOfProjects,  HttpStatus.OK));
    }

    @PutMapping("/manager/complete/{projectCode}")
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable("projectCode") String projectCode){

        projectService.complete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("project completed",  HttpStatus.OK));
    }
}
