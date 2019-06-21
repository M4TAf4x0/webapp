package com.archetype.monolithic.webapp.service.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class SignupFormModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

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
}
