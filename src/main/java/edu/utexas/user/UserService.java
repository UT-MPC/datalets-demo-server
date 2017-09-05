package edu.utexas.user;

import edu.utexas.datalet.Datalet;
import edu.utexas.datalet.DataletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private DataletService dataletService;

	public User createUser(User user) {
		if (!user.isValid()) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User is not valid");
		} else if (getUserByEmail(user.getEmail()) != null ) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
					"User with email '" + user.getEmail() + "' already exists");
		}

		return userRepo.save(user);
	}

	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	public User getUser(String id) {
		return userRepo.findOne(id);
	}

	public User getUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	public User getUserByDeviceID(String deviceID) {
		return userRepo.findByDeviceID(deviceID);
	}

	public User updateUser(String id, User user) {
		user.setId(id);

		List<Datalet> ownedDatalets = dataletService.getDataletsByOwner(user.getId());
		List<Datalet> dataletsToUpdate = ownedDatalets.stream()
				.filter(Datalet::isUseOwnerLocation)
				.map(d -> {
					d.setLocation(user.getLocation());
					return d;
				})
				.collect(Collectors.toList());
		dataletService.updateDatalets(dataletsToUpdate);

		return userRepo.save(user);
	}

	public void deleteUser(String id) {
		userRepo.delete(id);
	}

	public List<Datalet> getDataletsForUser(String id, boolean available) {
		if (available) {
			User user = getUser(id);
			List<Datalet> availableDatalets = dataletService.getAvailableDatalets();

			return availableDatalets.stream()
					.filter(d -> dataletService.evaluateDataletAvailabilityForUser(d, user))
					.collect(Collectors.toList());
		} else {
			return dataletService.getDataletsByOwner(id);
		}
	}
}
