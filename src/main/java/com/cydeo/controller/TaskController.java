package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.TaskService;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @RolesAllowed( "Manager")
    public ResponseEntity<ResponseWrapper> getTasks() {
        List<TaskDTO> listOfTasks = taskService.listAllTasks();
        return ResponseEntity.ok(new ResponseWrapper("task list", listOfTasks, HttpStatus.OK));

    }

    @GetMapping("/{taskId}")
    @RolesAllowed( "Manager")
    public ResponseEntity<ResponseWrapper> getTaskById(@PathVariable("taskId") Long taskId) {

        TaskDTO taskdto = taskService.findById(taskId);
        return ResponseEntity.ok(new ResponseWrapper("task found", taskdto, HttpStatus.OK));

    }

    @PostMapping
    @RolesAllowed( "Manager")
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO taskDTO) {
        taskService.save(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("task created", HttpStatus.CREATED));
    }


    @DeleteMapping("/{id}")
    @RolesAllowed( "Manager")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("id") Long id) {
        taskService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("task deleted", HttpStatus.OK));
    }

    @PutMapping// this is the method the manager will use to update
    @RolesAllowed( "Manager")
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO taskDTO) {
        taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task updated", HttpStatus.OK));
    }


    @GetMapping("/empoyee/pending-tasks")
    @RolesAllowed( "Employee")
    public ResponseEntity<ResponseWrapper> employeePendingTasks() {
       List<TaskDTO> incompleteTasks=taskService.listAllTasksByStatusIsNot(Status.COMPLETE);
        return ResponseEntity.ok(new ResponseWrapper("Incomplete Tasks",incompleteTasks, HttpStatus.OK));
    }

    @PutMapping("/employee/update/") // this is for the employee
    @RolesAllowed( "Employee")
    public ResponseEntity<ResponseWrapper> employeeUpdateTask(@RequestBody TaskDTO taskDTO) {
        taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task updated", HttpStatus.OK));
    }

    @GetMapping("/employee/archive")
    @RolesAllowed( "Employee")
    public ResponseEntity<ResponseWrapper> employeeArchivedTasks() {
        List<TaskDTO> completedTasks=taskService.listAllTasksByStatus(Status.COMPLETE);
        return ResponseEntity.ok(new ResponseWrapper("Incomplete Tasks",completedTasks, HttpStatus.OK));
    }

}

