package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.ProjectService;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Tag(name="ProjectController", description="Project API")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @RolesAllowed( "Manager")
    @Operation(summary="Get Projects")
    public ResponseEntity<ResponseWrapper> getProjects(){
       List<ProjectDTO> listOfProjects= projectService.listAllProjects();
        return ResponseEntity.ok(new ResponseWrapper("project list", listOfProjects, HttpStatus.OK));

    }
    @GetMapping("/{projectCode}")
    @RolesAllowed( "Manager")
    @Operation(summary="Get Project by Code")
    public ResponseEntity<ResponseWrapper> getProjectByCode(@PathVariable("projectCode") String projectCode){

       ProjectDTO project= projectService.getByProjectCode(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("project found", project, HttpStatus.OK));

    }
    @PostMapping
    @RolesAllowed({ "Admin","Manager"})
    @Operation(summary="Create Project")
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO){
        projectService.save(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("project created",  HttpStatus.CREATED));
    }
    @PutMapping
    @RolesAllowed( "Manager")
    @Operation(summary="Update Project")
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO){
        projectService.update(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("project updated",  HttpStatus.OK));
    }
    @DeleteMapping("/{code}")
    @RolesAllowed( "Manager")
    @Operation(summary="Delete Project")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("code") String code){
        projectService.delete(code);
        return ResponseEntity.ok(new ResponseWrapper("project deleted",  HttpStatus.OK));
    }
    @GetMapping("/manager/{project-status}")
    @RolesAllowed( "Manager")
    @Operation(summary="Get Project by Manager")
    public ResponseEntity<ResponseWrapper> getProjectByManager(){

        List<ProjectDTO> listOfProjects=projectService.listAllProjectDetails();
        return ResponseEntity.ok(new ResponseWrapper("projects by manager",listOfProjects,  HttpStatus.OK));
    }

    @PutMapping("/manager/complete/{projectCode}")
    @RolesAllowed( "Manager")
    @Operation(summary="Manager Complete Project")
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable("projectCode") String projectCode){

        projectService.complete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("project completed",  HttpStatus.OK));
    }
}
