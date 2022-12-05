package com.cydeo.dto;

import com.cydeo.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long id;

    @NotNull
    private ProjectDTO project;

    @NotNull
    private UserDTO assignedEmployee;

    @NotBlank
    private String taskSubject;

    @NotBlank
    private String taskDetail;

    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private Status taskStatus;
    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private LocalDate assignedDate;

}
