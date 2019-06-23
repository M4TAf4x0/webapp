package com.archetype.monolithic.webapp.controller;

import com.archetype.monolithic.webapp.controller.model.UserFormModel;
import com.archetype.monolithic.webapp.domain.Role;
import com.archetype.monolithic.webapp.domain.User;
import com.archetype.monolithic.webapp.errors.ResourceNotExistException;
import com.archetype.monolithic.webapp.repository.RoleRepository;
import com.archetype.monolithic.webapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Web controller for managing User.
 */
@Controller
public class UserController {

    private final Logger          log = LoggerFactory.getLogger(UserController.class);
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository  roleRepository;
    private final UserRepository  userRepository;

    public UserController(PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository, UserRepository userRepository) {

        this.passwordEncoder = passwordEncoder;
        this.roleRepository  = roleRepository;
        this.userRepository  = userRepository;
    }

    /**
     * GET  /users : get all the users.
     *
     * @param model the view model
     * @param page  the page
     * @return the users list view
     */
    @GetMapping("/users")
    public String getAllUsers(Model model, @RequestParam("page") Optional<Integer> page) {
        log.debug("GET request to get all users");

        Integer  currentPage = page.orElse(1);
        Pageable pageable    = PageRequest.of(currentPage - 1, 30, Sort.by("id").descending());
        model.addAttribute("userList", userRepository.findAll(pageable));

        return "user/userList";
    }

    /**
     * GET  /user/:uuid : get the user by uuid.
     *
     * @param uuid          the user uuid
     * @param userFormModel the user model
     * @param model         the view model
     * @return the user form view
     */
    @GetMapping("/user/{uuid}")
    public String viewUser(@PathVariable String uuid,
                           @ModelAttribute UserFormModel userFormModel,
                           Model model) {

        log.debug("GET request to get user : {}", uuid);

        model.addAttribute("rolesList", roleRepository.findAll());

        userRepository.findByUuid(uuid)
                .map(
                        user -> {
                            userFormModel.setEmail(user.getEmail());
                            userFormModel.setFirstName(user.getFirstName());
                            userFormModel.setLastName(user.getLastName());
                            userFormModel.setRoles(
                                    user.getRoles()
                                            .stream()
                                            .map(Role::getName)
                                            .collect(Collectors.toSet()));
                            return userFormModel;
                        })
                .orElseThrow(
                        () -> new ResourceNotExistException(
                                "The user : " + uuid + "does not exist."));

        return "user/userForm";
    }

    /**
     * GET  /user/:uuid : update user.
     *
     * @param uuid          the user uuid
     * @param userFormModel the user model
     * @param bindingResult the binding result
     * @param model         the view model
     * @return the user form view
     */
    @PostMapping("/user/{uuid}")
    public String updateUser(@PathVariable String uuid,
                             @Valid @ModelAttribute UserFormModel userFormModel,
                             BindingResult bindingResult, Model model) {

        model.addAttribute("rolesList", roleRepository.findAll());

        if (!bindingResult.hasErrors()) {

            User userUpdated = userRepository.findByUuid(uuid)
                    .map(user -> {
                        user.setEmail(userFormModel.getEmail().toLowerCase());
                        user.setFirstName(userFormModel.getFirstName());
                        user.setLastName(userFormModel.getLastName());
                        user.setRoles(
                                userFormModel.getRoles()
                                        .stream()
                                        .map(roleRepository::findByName)
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .collect(Collectors.toSet())
                        );

                        log.debug("POST request to update user : {}", uuid);
                        return userRepository.save(user);
                    })
                    .orElseThrow(
                            () -> new ResourceNotExistException(
                                    "The user : " + uuid + "does not exist."));

            if (userUpdated.getId() != null) {
                model.addAttribute("recordUpdated", true);
            }
        }

        return "user/userForm";
    }

    /**
     * GET  /users/:add : add a new user.
     *
     * @param userFormModel the user model
     * @param model         the view model
     * @return the user form view
     */
    @GetMapping("users/add")
    public String addUser(@ModelAttribute UserFormModel userFormModel, Model model) {
        log.debug("GET request to add a new user");

        model.addAttribute("rolesList", roleRepository.findAll());

        return "user/userForm";
    }
}