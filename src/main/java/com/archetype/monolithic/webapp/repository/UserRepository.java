package com.archetype.monolithic.webapp.repository;

import com.archetype.monolithic.webapp.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findOneWithRolesByEmailIgnoreCase(String email);

    Optional<User> findByUuid(String uuid);

    Page<User> findAll(Pageable pageable);
}