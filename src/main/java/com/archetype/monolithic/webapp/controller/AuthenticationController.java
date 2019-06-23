package com.archetype.monolithic.webapp.controller;

import com.archetype.monolithic.webapp.config.Constants;
import com.archetype.monolithic.webapp.controller.model.UserFormModel;
import com.archetype.monolithic.webapp.domain.User;
import com.archetype.monolithic.webapp.repository.RoleRepository;
import com.archetype.monolithic.webapp.repository.UserRepository;
import com.archetype.monolithic.webapp.security.RolesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Web controller for managing user authentication.
 */
@Controller
public class AuthenticationController {

    private final Logger          log = LoggerFactory.getLogger(AuthenticationController.class);
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository  roleRepository;
    private final UserRepository  userRepository;

    public AuthenticationController(PasswordEncoder passwordEncoder,
                                    RoleRepository roleRepository,
                                    UserRepository userRepository) {

        this.passwordEncoder = passwordEncoder;
        this.roleRepository  = roleRepository;
        this.userRepository  = userRepository;
    }

    /**
     * Authenticate a user.
     *
     * @return the login view
     */
    @GetMapping("/login")
    public String login() {
        return "authentication/login";
    }

    /**
     * Show the sign up view.
     *
     * @return the sign up view
     */
    @GetMapping("/signup")
    public String signup(@ModelAttribute UserFormModel userFormModel) {
        return "authentication/signup";
    }

    /**
     * Register a new user.
     *
     * @return the sign up view
     */
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute UserFormModel userFormModel,
                         BindingResult bindingResult) {
        log.debug("POST request to register User : {}", userFormModel);

        if (!bindingResult.hasErrors()) {

            User user = new User();
            user.setEmail(userFormModel.getEmail().toLowerCase());
            user.setFirstName(userFormModel.getFirstName());
            user.setLastName(userFormModel.getLastName());

            if (user.getLangKey() == null) {
                user.setLangKey(Constants.DEFAULT_LANGUAGE);
            }

            roleRepository.findByName(RolesConstants.USER).ifPresent(user::addRole);

            user.setPassword(passwordEncoder.encode(userFormModel.getPassword()));

            if (userRepository.save(user).getId() != null) {
                return "authentication/signup-success";
            }
        }

        return "authentication/signup";
    }
}