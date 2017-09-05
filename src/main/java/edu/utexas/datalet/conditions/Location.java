package edu.utexas.datalet.conditions;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.utexas.datalet.Datalet;
import edu.utexas.shared.GPSPoint;
import edu.utexas.shared.Rectangle;
import edu.utexas.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Location extends Condition {
	private static final Logger log = LoggerFactory.getLogger(Location.class);

	@JsonProperty("zone")
	private List<Rectangle> zone;
	@JsonProperty("point")
	private GPSPoint point;
	@JsonProperty("reference")
	private String reference;

	public Location() {
	}

	public Location(String form, String operand, String operator, GPSPoint point) {
		super(form, operator, operand);
		this.point = point;
	}

	@JsonIgnore
	@Override
	public boolean isSubTypeValid() {
		if (zone == null && reference == null && point == null) {
			return false;
		} else if ((zone != null && reference != null)
				|| (zone != null && point != null)
				|| (point != null && reference != null)) {
			return false;
		}

		return true;
	}

	@JsonIgnore
	public boolean isReference() {
		return reference != null;
	}

	public boolean evaluateLocation(GPSPoint comparePoint, Datalet datalet) {
		try {
			Double threshold = Double.parseDouble(operand);
			if (getZone() != null) {
				log.error("Use zone");
				switch (operator) {
					case ">":
						for (Rectangle r : zone) {
							if (r.distanceFromPoint(comparePoint) < threshold) {
								return false;
							}
						}

						return true;
					case "<":
						for (Rectangle r : zone) {
							if (r.distanceFromPoint(comparePoint) <= threshold) {
								return true;
							}
						}

						return false;
					default:
						return false;
				}
			} else if (getPoint() != null) {
				log.error("Use point");
				if (">".equals(operator)) {
					return getPoint().distance(comparePoint) >= threshold;
				} else if ("<".equals(operator)) {
					return getPoint().distance(comparePoint) <= threshold;
				} else {
					return false;
				}
			} else if (getReference() != null) {
				log.error("Use reference");
				//TODO: finish handling reference
			} else {
				log.error("Just use the datalet's location");
				GPSPoint location = datalet.getLocation();
				double distance = location.distance(comparePoint);
				log.error("Datalet point: " + location + ", Compare Point: " + comparePoint);
				log.error("Distance between points: " + distance);
				if (">".equals(operator)) {
					return distance >= threshold;
				} else if ("<".equals(operator)) {
					return distance <= threshold;
				} else {
					return false;
				}
			}
		} catch (NullPointerException | NumberFormatException ex) {
			return false;
		}

		return true;
	}

	public List<Rectangle> getZone() {
		return zone;
	}

	public void setZone(List<Rectangle> zone) {
		this.zone = zone;
	}

	public GPSPoint getPoint() {
		return point;
	}

	public void setPoint(GPSPoint point) {
		this.point = point;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	@Override
	public String toString() {
		return "Location{" + super.toString() +
				"zone=" + zone +
				", point='" + point + '\'' +
				", reference='" + reference + '\'' +
				'}';
	}
}
