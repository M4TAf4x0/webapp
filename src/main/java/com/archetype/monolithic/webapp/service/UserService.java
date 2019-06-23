package com.archetype.monolithic.webapp.service;

import com.archetype.monolithic.webapp.config.Constants;
import com.archetype.monolithic.webapp.domain.User;
import com.archetype.monolithic.webapp.errors.BadRequestException;
import com.archetype.monolithic.webapp.repository.RoleRepository;
import com.archetype.monolithic.webapp.repository.UserRepository;
import com.archetype.monolithic.webapp.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing User.
 */
@Service
@Transactional
public class UserService {

    private final Logger          log = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository  roleRepository;
    private final UserRepository  userRepository;

    public UserService(PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                       UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository  = roleRepository;
        this.userRepository  = userRepository;
    }

    /**
     * Get the current user.
     *
     * @return the current user
     */
    @Transactional(readOnly = true)
    public Optional<User> getCurrentUser() {
        log.debug("Request to get the current user");
        return SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneByEmailIgnoreCase);
    }

    /**
     * Register a new user.
     *
     * @param user the user to register
     * @return the persisted entity
     */
    public User registerUser(User user, String password) {
        log.debug("Request to register user : {}", user);

        if (user.getId() != null) {
            throw new BadRequestException("A new user cannot have an id");
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(user.getRoles()
                .stream()
                .map(role -> roleRepository.findByName(role.getName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet())
        );

        if (user.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE);
        }

        return userRepository.save(user);
    }


    /**
     * Get all users.
     *
     * @param pageable the pageable instance
     * @return the page of users
     */
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        log.debug("Request to get all users");
        return userRepository.findAll(pageable);
    }

    /**
     * Find user by uuid.
     *
     * @param uuid the uuid
     * @return the user
     */
    @Transactional(readOnly = true)
    public Optional<User> findOneByUuid(String uuid) {
        log.debug("Request to get user : {}", uuid);
        return userRepository.findByUuid(uuid);
    }
}