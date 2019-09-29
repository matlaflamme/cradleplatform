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
 * Controller for Login to handle routes to all the users (atm)
 * Most of these handlers are for testing purposes. 
 * TODO: Handle post/get requests from client/frontend
 * 
 * @see User
 */
@Controller
public class LoginController {
	private UserRepository userRepository;

	public LoginController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String root(Model model, Principal user) {
		if (user != null) {
			model.addAttribute("accountName", "User: " + user.getName());
		} else {
			model.addAttribute("accountName", "Please login");
		}
		return "home";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(Model model, Principal user) {
		if (user != null) {
			model.addAttribute("accountName", "User: " + user.getName());
		} else {
			model.addAttribute("accountName", "Please login");
		}
		return "admin";
	}

	@RequestMapping(value = "/vht", method = RequestMethod.GET)
	public String vhtPage(Model model, Principal user) {
		if (user != null) {
			model.addAttribute("accountName", "User: " + user.getName());
		} else {
			model.addAttribute("accountName", "Please login");
		}
		return "vht";
	}

	@RequestMapping(value = "/healthworker", method = RequestMethod.GET)
	public String login(Model model, Principal user) {
		if (user != null) {
			model.addAttribute("accountName", "User: " + user.getName());
		} else {
			model.addAttribute("accountName", "Please login");
		}
		return "healthworker";
	}
<<<<<<< web/src/main/java/com/cradlerest/web/controller/LoginController.java
// TODO: Login post handler
//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//	public String login(Model mode, String error, String logout) {
//    	if (error) {
//    		model.addAttribute("error", "Your username and password is invalid");
//		}
//	}
=======
>>>>>>> web/src/main/java/com/cradlerest/web/controller/LoginController.java

}
