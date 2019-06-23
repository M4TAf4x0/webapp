package com.archetype.monolithic.webapp.controller.user;

import com.archetype.monolithic.webapp.controller.model.UserFormModel;
import com.archetype.monolithic.webapp.domain.Role;
import com.archetype.monolithic.webapp.domain.User;
import com.archetype.monolithic.webapp.errors.ResourceNotExistException;
import com.archetype.monolithic.webapp.service.RoleService;
import com.archetype.monolithic.webapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final Logger      log = LoggerFactory.getLogger(UserController.class);
    private final RoleService roleService;
    private final UserService userService;

    public UserController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * GET  /users : get all the users.
     *
     * @param model the View model
     * @param page  the page
     * @return the users list view
     */
    @GetMapping("/users")
    public String getAllUsers(Model model, @RequestParam("page") Optional<Integer> page) {
        log.debug("GET request to get all users");

        Integer  currentPage = page.orElse(1);
        Pageable pageable    = PageRequest.of(currentPage - 1, 30, Sort.by("id").descending());
        model.addAttribute("userList", userService.findAll(pageable));

        return "user/list";
    }

    /**
     * GET  /user/:uuid : get the user by uuid.
     *
     * @param uuid          the uuid
     * @param userFormModel the User model
     * @param model         the View model
     * @return the user
     */
    @GetMapping("/user/{uuid}")
    public String viewUser(@PathVariable String uuid,
                           @ModelAttribute UserFormModel userFormModel,
                           Model model) {
        log.debug("GET request to get user : {}", uuid);

        User user = userService.findOneByUuid(uuid)
                .orElseThrow(ResourceNotExistException::new);

        model.addAttribute("rolesList", roleService.findAllRoleNames());

        userFormModel.setEmail(user.getEmail());
        userFormModel.setFirstName(user.getFirstName());
        userFormModel.setLastName(user.getLastName());
        userFormModel.setRoles(
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()));

        return "user/view";
    }

    @PostMapping("/user/{uuid}")
    public String updateUser(@PathVariable String uuid,
                             @Valid @ModelAttribute UserFormModel userFormModel,
                             BindingResult bindingResult, Model model) {

        model.addAttribute("rolesList", roleService.findAllRoleNames());

        if (!bindingResult.hasErrors()) {
            userService.findOneByUuid(uuid).ifPresent(user -> {
                user.setEmail(userFormModel.getEmail().toLowerCase());
                user.setFirstName(userFormModel.getFirstName());
                user.setLastName(userFormModel.getLastName());

            });




           /* User updateUser = new User();

            updateUser.setEmail(userFormModel.getEmail().toLowerCase());
            updateUser.setFirstName(userFormModel.getFirstName());
            updateUser.setLastName(userFormModel.getLastName());

            if (userService.registerUser(updateUser, userFormModel.getPassword()).getId() != null) {
                return "authentication/signup-success";
            }*/
        }

        return "user/view";
    }

}
