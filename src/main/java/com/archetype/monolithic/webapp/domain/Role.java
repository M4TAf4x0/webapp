package com.archetype.monolithic.webapp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A Role.
 */
@Entity
@Table(name = "role")
@Getter
@Setter
@ToString
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(length = 50, nullable = false, unique = true)
    private String name;

    public Role() {

    }

    public Role(@NotNull @Size(max = 50) String name) {
        this.name = name;
    }
}