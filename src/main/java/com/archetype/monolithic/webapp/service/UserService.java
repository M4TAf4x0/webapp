package com.archetype.monolithic.webapp.service;

import com.archetype.monolithic.webapp.config.Constants;
import com.archetype.monolithic.webapp.domain.User;
import com.archetype.monolithic.webapp.errors.BadRequestException;
import com.archetype.monolithic.webapp.repository.RoleRepository;
import com.archetype.monolithic.webapp.repository.UserRepository;
import com.archetype.monolithic.webapp.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public UserService(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository) {
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
        log.debug("Request to get the current User");
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByEmail);
    }

    /**
     * Register a user.
     *
     * @param user     the user to register
     * @param password the user password
     * @return the persisted entity
     */
    @Transactional(readOnly = true)
    public User registerUser(User user, String password) {
        log.debug("Request to register User : {}", user);

        if (user.getId() != null) {
            throw new BadRequestException("A new user can not have an id");
        }

        if (user.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE);
        }

        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }
}