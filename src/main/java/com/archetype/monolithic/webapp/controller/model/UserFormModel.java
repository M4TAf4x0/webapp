package com.archetype.monolithic.webapp.controller.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A view model for the User entity.
 */
@Getter
@Setter
@ToString
public class UserFormModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Email
    @NotEmpty
    private String email;

    @Size(min = 7, max = 30)
    private String password;

    @NotEmpty
    @Size(max = 50)
    private String firstName;

    @NotEmpty
    @Size(max = 50)
    private String lastName;

    private Set<String> roles = new HashSet<>();
}
