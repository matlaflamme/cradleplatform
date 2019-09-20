package com.cradlerest.web.controller;

import com.cradlerest.web.controller.error.DatabaseException;
import com.cradlerest.web.controller.error.EntityNotFoundException;
import com.cradlerest.web.model.User;
import com.cradlerest.web.service.repository.UserRepository;
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
 * - {@code GET:/mock-api/user/all}: return all users in the database
 * - {@code GET:/mock-api/user/{id}}: return the user with a given {@code id}
 * - {@code POST:/mock-api/user/add}: create a new user
 * - {@code DELETE:/mock-api/user/{id}}: delete the user with a given {@code id}
 *
 * @see User
 */
@RestController
@RequestMapping("/mock-api/user")
public class MockUserController {

	private UserRepository userRepository;

	public MockUserController(UserRepository userRepository) {
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

	@PostMapping("/add")
	public User create(@RequestBody Map<String, String> body) {
		String userId = body.get("userId");
		String password = body.get("password");
		return userRepository.save(new User(userId, password));
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
