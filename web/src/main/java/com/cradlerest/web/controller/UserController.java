package com.cradlerest.web.controller;

import com.cradlerest.web.constraints.user.RoleValidator;
import com.cradlerest.web.controller.exceptions.AlreadyExistsException;
import com.cradlerest.web.controller.exceptions.DatabaseException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.PatientWithLatestReadingView;
import com.cradlerest.web.model.User;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.ReadingManager;
import com.cradlerest.web.model.UserDetailsImpl;
import com.cradlerest.web.service.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private PasswordEncoder  passwordEncoder;
	private PatientManagerService patientManagerService;
	private ReadingManager readingManager;

	public UserController(UserRepository userRepository,
						  PasswordEncoder passwordEncoder,
						  PatientManagerService patientManagerService,
						  ReadingManager readingManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.patientManagerService = patientManagerService;
		this.readingManager = readingManager;
	}

	@GetMapping("/all")
	public @ResponseBody List<User> all() {
		return userRepository.findAll();
	}

	@GetMapping("/{id}")
	public @ResponseBody User get(@PathVariable("id") int id) throws DatabaseException {
		Optional<User> optUser = userRepository.findById(id);

		if (optUser.isEmpty()) {
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

	/**
	 * Swaps the active value of a user
	 * If user is not active, they cannot be authenticated
	 *
	 * @param username A username.
	 * @return A response message.
	 * @throws EntityNotFoundException If no user with the given user name exists.
	 */
	@PostMapping("/{username}/change-active")
	public String updateUserIsActive(@PathVariable("username") String username) throws EntityNotFoundException{
		Optional<User> foundUser = userRepository.findByUsername(username);
		if (foundUser.isPresent()) {
			foundUser.get().swapActive();
			userRepository.save(foundUser.get());
			return "User active set to (1=true,0=false): " + foundUser.get().getActive();
		} else {
			throw new EntityNotFoundException("user not found: " + username);
		}
	}

	/**
	 * Associates a specified user with a given health centre. This method may
	 * be used to update or add a new association. Use "remove-health-centre"
	 * to delete an existing association.
	 * @param username The username of the user to update.
	 * @param healthCentreId The id of the health centre to associate with.
	 * @throws EntityNotFoundException If unable to find the requested user or
	 * 	health centre.
	 */
	@PostMapping("/{username}/set-health-centre")
	public void updateWorksAtHealthCentreId(@PathVariable("username") String username,
											@RequestParam("hcid") int healthCentreId)
			throws EntityNotFoundException {
		try {
			userRepository.updateWorksAtByUsername(username, healthCentreId);
		} catch (Exception e) {
			throw new EntityNotFoundException("unable to find user or health centre", e);
		}
	}

	/**
	 * Removes a health centre association for a given user.
	 * @param username The username of the user to update.
	 * @throws EntityNotFoundException If unable to find the user.
	 */
	@PostMapping("/{username}/remove-health-centre")
	public void deleteWorksAtHealthCentreId(@PathVariable("username") String username) throws EntityNotFoundException {
		try {
			userRepository.updateWorksAtByUsername(username, null);
		} catch (Exception e) {
			throw new EntityNotFoundException("unable to find user", e);
		}
	}

	/**
	 * Returns the id of the health centre that a given user is associated with.
	 * May be {@code null}.
	 * @param username The username of the user to look for.
	 * @return The id of the health centre the user is associated with, or
	 * 	{@code null} if the user is not affiliated with any centre.
	 * @throws EntityNotFoundException If unable to find the user.
	 */
	@GetMapping("/{username}/health-centre")
	public Object getHealthCentre(@PathVariable("username") String username) throws EntityNotFoundException {
		class Result {
			public Integer id;

			private Result(Integer id) {
				this.id = id;
			}
		}
		Optional<User> optUser = userRepository.findByUsername(username);
		if (optUser.isEmpty()) {
			throw new EntityNotFoundException("unable to find user with username: " + username);
		}
		var id = optUser.get().getWorksAtHealthCentreId();
		return new Result(id);
	}


	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") int id) throws DatabaseException {
		try {
			userRepository.deleteById(id);
		} catch (Exception e) {
			throw new EntityNotFoundException(id);
		}
	}

	/**
	 * Returns a list of patients who have readings created by a given user. If
	 * unable to find a user with the given id, an empty list is returned.
	 * @param id A user id.
	 * @return A list of patients with their latest readings.
	 */
	@GetMapping("/{id}/patients")
	public List<PatientWithLatestReadingView> patients(@PathVariable("id") int id) {
		return patientManagerService.getPatientsWithReadingsCreatedBy(id);
	}

	/**
	 * Returns a list of all readings created by a given user. If unable to
	 * find a user with the given id, an empty list is returned.
	 * @param id A user id.
	 * @return A list of readings created by this user.
	 */
	@GetMapping("/{id}/readings")
	public List<ReadingView> readings(@PathVariable("id") int id) {
		return readingManager.getAllCreatedBy(id);
	}

	/**
	 * A test API method which returns the username of the requesting user.
	 * @return The requesting user's username.
	 */
	@GetMapping("/whoami")
	public Object whoAmI() {
		class Result {
			public String username;

			private Result(String username) {
				this.username = username;
			}
		}

		var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsImpl) {
			var userDetails = (UserDetailsImpl) principal;
			return new Result(userDetails.getUsername());
		} else {
			return principal;
		}
	}
}
