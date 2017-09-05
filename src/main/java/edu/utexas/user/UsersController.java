package edu.utexas.user;

import edu.utexas.datalet.Datalet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UsersController {
	private static final Logger log = LoggerFactory.getLogger(UsersController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@RequestMapping(method = RequestMethod.POST)
	public User createUser(@RequestBody User user) {
		log.error(user.toString());
		return userService.createUser(user);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public User getUser(@PathVariable String id) {
		return userService.getUser(id);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public User updateUser(@PathVariable String id,
						   @RequestBody User user) {
		return userService.updateUser(id, user);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deleteUser(@PathVariable String id) {
		userService.deleteUser(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/datalets")
	public List<Datalet> getDataletsForUser(@PathVariable String id,
											@RequestParam(defaultValue="false") boolean available) {
		return userService.getDataletsForUser(id, available);
	}
}
