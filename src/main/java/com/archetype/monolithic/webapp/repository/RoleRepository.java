package com.archetype.monolithic.webapp.repository;

import com.archetype.monolithic.webapp.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Role entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}