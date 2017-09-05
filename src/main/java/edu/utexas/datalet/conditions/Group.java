package edu.utexas.datalet.conditions;


import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.utexas.user.User;

import java.util.List;

public class Group extends Condition {

	@JsonIgnore
	@Override
	boolean isSubTypeValid() {
		return true;
	}

	public Group() {
	}

	public boolean evaluateGroup(User user) {
		if (user != null && user.getGroups() != null) {
			List<String> groups = user.getGroups();
			switch (operator) {
				case "=":
					return groups.contains(operand);
				case "!=":
					return !groups.contains(operand);
				default:
					return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Group{" + super.toString() +
				"}";
	}
}
