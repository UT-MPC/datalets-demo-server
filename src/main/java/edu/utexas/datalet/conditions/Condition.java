package edu.utexas.datalet.conditions;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "@form")
@JsonSubTypes({
		@Type(value = Schedule.class, name = "schedule"),
		@Type(value = Location.class, name = "location"),
		@Type(value = Profile.class, name = "profile"),
		@Type(value = Group.class, name = "group")
})
public abstract class Condition {

	@JsonProperty("form")
	protected String form;
	@JsonProperty("operator")
	protected String operator;
	@JsonProperty("operand")
	protected String operand;

	public Condition() {}

	public Condition(String form, String operator, String operand) {
		this.form = form;
		this.operator = operator;
		this.operand = operand;
	}

	@JsonIgnore
	public boolean isValid() {
		if (form == null || operator == null || operand == null) {
			return false;
		}

		return isSubTypeValid();
	}

	abstract boolean isSubTypeValid();

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperand() {
		return operand;
	}

	public void setOperand(String operand) {
		this.operand = operand;
	}

	@Override
	public String toString() {
		return "Condition{" +
				"form='" + form + '\'' +
				", operator='" + operator + '\'' +
				", operand='" + operand + '\'' +
				'}';
	}
}
