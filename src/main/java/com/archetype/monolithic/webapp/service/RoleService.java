package com.archetype.monolithic.webapp.service;

import com.archetype.monolithic.webapp.domain.Role;
import com.archetype.monolithic.webapp.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Role.
 */
@Service
@Transactional
public class RoleService {

    private final Logger         log = LoggerFactory.getLogger(RoleService.class);
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Get all roles.
     *
     * @return the list of roles
     */
    @Transactional(readOnly = true)
    public List<Role> findAllRoles() {
        log.debug("Request to get all roles");
        return roleRepository.findAll();
    }

    /**
     * Get all role names.
     *
     * @return the list of role names
     */
    public Set<String> findAllRoleNames() {
        return roleRepository.findAll()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
