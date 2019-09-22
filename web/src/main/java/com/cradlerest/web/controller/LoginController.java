package com.cradlerest.web.controller;
import com.cradlerest.web.controller.error.DatabaseException;
import com.cradlerest.web.controller.error.EntityNotFoundException;
import com.cradlerest.web.model.User;
import com.cradlerest.web.service.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Dummy controller for testing database interaction with {@code User} entities.
 *
 * Simply provides an API for creating, querying, and deleting {@code User}
 * entities within the database.
 *
 * Provides the following endpoints:
 *
 * @see User
 */
@Controller
public class LoginController {

    private UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/")
    public String root() {
        return "home";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }
}
