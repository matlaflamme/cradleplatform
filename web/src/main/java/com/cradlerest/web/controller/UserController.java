package com.cradlerest.web.controller;

import com.cradlerest.web.constraints.user.RoleValidator;
import com.cradlerest.web.controller.exceptions.AlreadyExistsException;
import com.cradlerest.web.controller.exceptions.DatabaseException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.User;
import com.cradlerest.web.service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Dummy controller for testing database interaction with {@code User} entities.
 *
 * Simply provides an API for creating, querying, and deleting {@code User}
 * entities within the database.
 *
 * Provides the following endpoints:
 *
 * - {@code GET:/api/user/all}: return all users in the database
 * - {@code GET:/api/user/{id}}: return the user with a given {@code id}
 * - {@code POST:/api/user/add}: create a new user
 * - {@code DELETE:/api/user/{id}}: delete the user with a given {@code id}
 *
 * @see User
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder  passwordEncoder;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/all")
	public @ResponseBody List<User> all() {
		return userRepository.findAll();
	}

	@GetMapping("/{id}")
	public @ResponseBody User get(@PathVariable("id") int id) throws DatabaseException {
		Optional<User> optUser = userRepository.findById(id);

		if (!optUser.isPresent()) {
			throw new EntityNotFoundException(id);
		}
		return optUser.get();
	}

	/**
	 * User constraints:
	 * Username min length: 6
	 * Password min length: 8
	 * @see RoleValidator for role constraints
	 */
	@PostMapping("/add")
	@ResponseStatus(HttpStatus.CREATED)
	public User create(@Valid @RequestBody User user) throws AlreadyExistsException {
		String username = user.getUsername();
		String password = passwordEncoder.encode(user.getPassword());
		String roles = user.getRoles();
		if (userRepository.findByUsername(username).isPresent()) {
			throw new AlreadyExistsException(username);
		}
		System.out.println("Created user: " + username);
		return userRepository.save(new User(username, password, roles));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") int id) throws DatabaseException {
		try {
			userRepository.deleteById(id);
		} catch (Exception e) {
			throw new EntityNotFoundException(id);
		}
	}
}
