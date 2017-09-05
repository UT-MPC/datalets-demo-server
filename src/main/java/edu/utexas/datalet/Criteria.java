package edu.utexas.datalet;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.utexas.datalet.conditions.Condition;

import java.util.List;

@JsonDeserialize(using = CriteriaDeserializer.class)
public class Criteria {

	@JsonProperty("type")
	private String type;
	@JsonProperty("children")
	private List<Criteria> children;
	@JsonProperty("condition")
	private Condition condition;

	public Criteria() {
	}

	@JsonIgnore
	public boolean isValid() {
		if (isOperator()) {
			if (children == null || condition != null) {
				return false;
			} else {
				for (Criteria child : children) {
					if (!child.isValid()) {
						return false;
					}
				}
			}
		} else if (isCondition()) {
			if (condition == null || children != null) {
				return false;
			} else if (!condition.isValid()) {
				return false;
			}
		} else {
			return false;
		}

		return true;
	}

	@JsonIgnore
	public boolean isOperator() {
		return "and".equals(type) || "or".equals(type);
	}

	@JsonIgnore
	public boolean isCondition() {
		return "condition".equals(type);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Criteria> getChildren() {
		return children;
	}

	public void setChildren(List<Criteria> children) {
		this.children = children;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "Criteria{" +
				"type='" + type + '\'' +
				", children=" + children +
				", condition=" + condition +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Criteria criteria = (Criteria) o;

		if (type != null ? !type.equals(criteria.type) : criteria.type != null) return false;
		if (children != null ? !children.equals(criteria.children) : criteria.children != null) return false;
		return condition != null ? condition.equals(criteria.condition) : criteria.condition == null;

	}

	@Override
	public int hashCode() {
		int result = type != null ? type.hashCode() : 0;
		result = 31 * result + (children != null ? children.hashCode() : 0);
		result = 31 * result + (condition != null ? condition.hashCode() : 0);
		return result;
	}
}
