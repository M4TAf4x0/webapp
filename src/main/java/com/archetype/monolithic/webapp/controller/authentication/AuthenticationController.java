package com.archetype.monolithic.webapp.controller.authentication;

import com.archetype.monolithic.webapp.controller.authentication.model.SignupFormModel;
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
    public String signup(@ModelAttribute SignupFormModel signupFormModel) {
        return "authentication/signup";
    }

    /**
     * Register a new user.
     *
     * @return the sign up view
     */
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignupFormModel signupFormModel, BindingResult bindingResult) {
        log.debug("POST request to register User : {}", signupFormModel);

        if (!bindingResult.hasErrors()) {
            User newUser = new User();

            newUser.setEmail(signupFormModel.getEmail().toLowerCase());
            newUser.setFirstName(signupFormModel.getFirstName());
            newUser.setLastName(signupFormModel.getLastName());
            newUser.addRole(new Role(RolesConstants.USER));

            if (userService.registerUser(newUser, signupFormModel.getPassword()).getId() != null) {
                return "";
            }
        }

        return "authentication/signup";
    }
}