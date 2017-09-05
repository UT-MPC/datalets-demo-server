package edu.utexas.datalet;

import edu.utexas.datalet.conditions.*;
import edu.utexas.shared.GPSPoint;
import edu.utexas.user.User;
import edu.utexas.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataletService {

	@Autowired
	private DataletRepository dataletRepo;
	@Autowired
	private UserService userService;

	public Datalet createDatalet(Datalet datalet) {
		if (evaluateDataletPrecondition(datalet)) {
			datalet.setTimeAvailable(System.currentTimeMillis());
		} else {
			datalet.setTimeAvailable(null);
		}
		return dataletRepo.save(datalet);
	}

	public List<Datalet> getAllDatalets() {
		return dataletRepo.findAll();
	}

	public Datalet getDatalet(String id) {
		return dataletRepo.findOne(id);
	}

	public Datalet updateDatalet(String id, Datalet datalet) {
		if (evaluateDataletPrecondition(datalet)) {
			datalet.setTimeAvailable(System.currentTimeMillis());
		} else {
			datalet.setTimeAvailable(null);
		}
		return dataletRepo.save(datalet);
	}

	public void deleteDatalet(String id) {
		dataletRepo.delete(id);
	}

	public List<User> getUsersWithAccessTo(String dataletID) {
		Datalet datalet = getDatalet(dataletID);

		boolean available = evaluateDataletPrecondition(datalet);

		if (!available) {
			return new ArrayList<>();
		}

		List<User> users = userService.getAllUsers();

		return users.stream()
				.filter(u -> evaluateDataletAvailabilityForUser(datalet, u))
				.collect(Collectors.toList());
	}

	public List<Datalet> getDataletsByOwner(String ownerID) {
		return dataletRepo.findByOwnerID(ownerID);
	}

	public List<Datalet> updateDatalets(List<Datalet> datalets) {
		return dataletRepo.save(datalets);
	}

	public List<Datalet> getAvailableDatalets() {
		//TODO: check each datalet for ones that have preconditions that are met
		List<Datalet> datalets = getAllDatalets();

		return datalets.stream()
				.filter(this::evaluateDataletPrecondition)
				.collect(Collectors.toList());

//		return dataletRepo.findByTimeAvailableIsSet();
	}

	public void evaluateAllDataletsPreconditions() {
		//TODO: be able to evaluate preconditions for all datalets by scheduling a task to do so
	}

	public boolean evaluateDataletPrecondition(Datalet datalet) {
		User owner = userService.getUser(datalet.getOwnerID());
		return evaluateCriteria(datalet.getPrecondition(), owner, datalet, true);
	}

	public boolean evaluateDataletAvailabilityForUser(Datalet datalet, User user) {
		return evaluateCriteria(datalet.getAvailability(), user, datalet, false);
	}

	private boolean evaluateCriteria(Criteria criteria, User user, Datalet datalet, boolean precondition) {
		if (criteria != null) {
			if (criteria.isOperator()) {
				List<Boolean> childrenValues = criteria.getChildren().stream()
						.map(c -> evaluateCriteria(c, user, datalet, precondition))
						.collect(Collectors.toList());
				if ("and".equals(criteria.getType())) {
					return !childrenValues.contains(false);
				} else if ("or".equals(criteria.getType())) {
					return childrenValues.contains(true);
				}
			} else if (criteria.isCondition()) {
				return evaluateCondition(criteria.getCondition(), user, datalet, precondition);
			}
		}

		return true;
	}

	private boolean evaluateCondition(Condition condition, User user, Datalet datalet, boolean precondition) {
		if (condition instanceof Location) {
			GPSPoint comparePoint = user.getLocation();
			if (precondition) {
				comparePoint = datalet.getLocation();
			}
			return ((Location) condition).evaluateLocation(comparePoint, datalet);
		} else if (condition instanceof Schedule) {
			return ((Schedule) condition).evaluateSchedule();
		} else if (condition instanceof Profile) {
			return ((Profile) condition).evaluateProfile(user);
		} else if (condition instanceof Group) {
			return ((Group) condition).evaluateGroup(user);
		}

		return false;
	}
}
