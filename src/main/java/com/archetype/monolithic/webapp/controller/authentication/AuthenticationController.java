package com.archetype.monolithic.webapp.controller.authentication;

import com.archetype.monolithic.webapp.controller.model.UserFormModel;
import com.archetype.monolithic.webapp.domain.Role;
import com.archetype.monolithic.webapp.domain.User;
import com.archetype.monolithic.webapp.security.RolesConstants;
import com.archetype.monolithic.webapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger      log = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
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
            User newUser = new User();

            newUser.setEmail(userFormModel.getEmail().toLowerCase());
            newUser.setFirstName(userFormModel.getFirstName());
            newUser.setLastName(userFormModel.getLastName());
            newUser.addRole(new Role(RolesConstants.USER));

            if (userService.registerUser(newUser, userFormModel.getPassword()).getId() != null) {
                return "authentication/signup-success";
            }
        }

        return "authentication/signup";
    }
}