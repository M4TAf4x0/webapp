package com.archetype.monolithic.webapp.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class UserDTO {

    private Long         id;
    private String       uuid;
    private String       email;
    private String       password;
    private String       firstName;
    private String       lastName;
    private String       langKey;
    private Set<RoleDTO> roles = new HashSet<>();

}
