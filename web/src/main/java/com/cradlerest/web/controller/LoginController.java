package com.cradlerest.web.controller;
import com.cradlerest.web.controller.error.DatabaseException;
import com.cradlerest.web.controller.error.EntityNotFoundException;
import com.cradlerest.web.model.User;
import com.cradlerest.web.model.UserDetailsImpl;
import com.cradlerest.web.service.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for Login
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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String login(Model model, Principal user) {
        if (user != null) {
            model.addAttribute("accountName", "User: " + user.getName());
        } else {
            model.addAttribute("accountName", "Please login");
        }
        return "home";
    }

}
