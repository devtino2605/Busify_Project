package com.busify.project.role.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDTO implements Serializable {
    private Integer id;
    private String name;

}