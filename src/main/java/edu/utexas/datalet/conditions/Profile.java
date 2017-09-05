package edu.utexas.datalet.conditions;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.utexas.user.User;

public class Profile extends Condition {

	@JsonProperty("attribute")
	private String attribute;

	public Profile() {
	}

	@JsonIgnore
	@Override
	public boolean isSubTypeValid() {
		return attribute == null;
	}

	public boolean evaluateProfile(User user) {
		try {
			String[] attParts = attribute.split("\\.");
			String fieldValue = null;
			switch (attParts[0]) {
				case "id":
					fieldValue = user.getId();
					break;
				case "firstName":
					fieldValue = user.getFirstName();
					break;
				case "lastName":
					fieldValue = user.getLastName();
					break;
				case "gender":
					fieldValue = user.getGender();
					break;
				case "age":
					int testAge = Integer.parseInt(operand);
					long userAge = user.getAge();
					switch (operator) {
						case "=":
							return userAge == testAge;
						case ">":
							return userAge > testAge;
						case ">=":
							return userAge >= testAge;
						case "<=":
							return userAge <= testAge;
						case "<":
							return userAge < testAge;
						default:
							return false;
					}
				case "application":
					String shortName = attribute.substring(attribute.indexOf(".") + 1);
					if (user.getApplication() != null) {
						fieldValue = user.getApplication().get(shortName);
					}
					break;
			}

			if (fieldValue == null) {
				return false;
			}

			if ("!=".equals(operator)) {
				return !operand.toLowerCase().equals(fieldValue.toLowerCase());
			} else if ("range".equals(operator)) {
				int userValue = Integer.parseInt(fieldValue);
				String[] range = operand.split(",");

				return userValue >= Integer.parseInt(range[0].trim()) && userValue <= Integer.parseInt(range[1].trim());
			} else {
				return operand.toLowerCase().equals(fieldValue.toLowerCase());
			}


		} catch (Exception ex) {
			return false;
		}
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	@Override
	public String toString() {
		return "Profile{" + super.toString() +
				"attribute='" + attribute + '\'' +
				'}';
	}
}
