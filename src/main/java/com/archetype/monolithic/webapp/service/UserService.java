package com.archetype.monolithic.webapp.service;

import com.archetype.monolithic.webapp.config.Constants;
import com.archetype.monolithic.webapp.domain.User;
import com.archetype.monolithic.webapp.repository.RoleRepository;
import com.archetype.monolithic.webapp.repository.UserRepository;
import com.archetype.monolithic.webapp.security.RolesConstants;
import com.archetype.monolithic.webapp.security.SecurityUtils;
import com.archetype.monolithic.webapp.service.dto.RoleDTO;
import com.archetype.monolithic.webapp.service.dto.UserDTO;
import com.archetype.monolithic.webapp.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing User.
 */
@Service
@Transactional
public class UserService {

    private final Logger          log = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository  roleRepository;
    private final UserMapper      userMapper;
    private final UserRepository  userRepository;

    public UserService(PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                       UserMapper userMapper, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository  = roleRepository;
        this.userMapper      = userMapper;
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
     * @param userDTO  the user to register
     * @param password the user password
     * @return the persisted entity
     */
    @Transactional(readOnly = true)
    public UserDTO registerUser(UserDTO userDTO, String password) {
        log.debug("Request to register User : {}", userDTO);


        if (userDTO.getLangKey() == null) {
            userDTO.setLangKey(Constants.DEFAULT_LANGUAGE);
        }

        userDTO.setPassword(passwordEncoder.encode(password));

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(RolesConstants.USER);
        Set<RoleDTO> roles = new HashSet<>();
        roles.add(roleDTO);
        userDTO.setRoles(roles);
        User user = userMapper.toEntity(userDTO);
        log.debug(user.toString());

        return userMapper.toDto(userRepository.save(user));
    }
}