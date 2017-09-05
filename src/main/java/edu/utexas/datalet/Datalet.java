package edu.utexas.datalet;


import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.utexas.shared.GPSPoint;
import edu.utexas.user.User;
import org.springframework.data.annotation.Id;

import java.util.Map;

public class Datalet {

	@Id
	private String id;
	private String ownerID;
	private String title;
	private String description;
	private GPSPoint location;
	private boolean useOwnerLocation = true;
	private Long timeAvailable;
	private Map<String, String> data;
	private Criteria precondition;
	private Criteria availability;

	public Datalet() {

	}

	@JsonIgnore
	public boolean isValid() {
		if (ownerID == null || location == null || data == null) return false;
		if (precondition != null && !precondition.isValid()) return false;
		if (availability != null && !availability.isValid()) return false;

		return true;
	}

	@JsonIgnore
	public boolean isAvailable(User user) {
		return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public GPSPoint getLocation() {
		return location;
	}

	public void setLocation(GPSPoint location) {
		this.location = location;
	}

	public boolean isUseOwnerLocation() {
		return useOwnerLocation;
	}

	public void setUseOwnerLocation(boolean useOwnerLocation) {
		this.useOwnerLocation = useOwnerLocation;
	}

	public Long getTimeAvailable() {
		return timeAvailable;
	}

	public void setTimeAvailable(Long timeAvailable) {
		this.timeAvailable = timeAvailable;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public Criteria getPrecondition() {
		return precondition;
	}

	public void setPrecondition(Criteria precondition) {
		this.precondition = precondition;
	}

	public Criteria getAvailability() {
		return availability;
	}

	public void setAvailability(Criteria availability) {
		this.availability = availability;
	}

	@Override
	public String toString() {
		return "Datalet{" +
				"id='" + id + '\'' +
				", ownerID='" + ownerID + '\'' +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", location=" + location +
				", useOwnerLocation=" + useOwnerLocation +
				", timeAvailable=" + timeAvailable +
				", data=" + data +
				", precondition=" + precondition +
				", availability=" + availability +
				'}';
	}
}
