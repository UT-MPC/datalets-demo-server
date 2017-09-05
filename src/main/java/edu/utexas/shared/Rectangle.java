package edu.utexas.shared;

public class Rectangle {

	public final GPSPoint min ;
	public final GPSPoint max;

	public Rectangle() {
		min = new GPSPoint();
		max = new GPSPoint();
	}

	public Rectangle(GPSPoint min, GPSPoint max) {
		this.min = min;
		this.max = max;
	}

	public double distanceFromPoint(GPSPoint point) {
		if (point.latitude >= min.latitude && point.latitude <= max.latitude
				&& point.longitude >= min.longitude && point.longitude <= max.longitude) {
			return 0.0;
		}

		double dMaxLon = point.longitude - max.longitude;
		double dMaxLat = point.latitude - max.latitude;
		double dMinLon = min.longitude - point.longitude;
		double dMinLat = min.latitude - point.latitude;

		double closeLon = dMaxLon >= dMinLon ? max.longitude : min.longitude;
		double closeLat = dMaxLat >= dMinLat ? max.latitude : min.latitude;

		GPSPoint sameLon = new GPSPoint(closeLat, point.longitude);
		GPSPoint sameLat = new GPSPoint(point.latitude, closeLon);
		GPSPoint closeCorner = new GPSPoint(closeLat, closeLon);

		double min = Math.min(sameLon.distance(point), sameLat.distance(point));

		return Math.min(min, closeCorner.distance(point));
	}

	@Override
	public String toString() {
		return "Rectangle{" +
				"min=" + min +
				", max=" + max +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Rectangle rectangle = (Rectangle) o;

		if (min != null ? !min.equals(rectangle.min) : rectangle.min != null) return false;
		return max != null ? max.equals(rectangle.max) : rectangle.max == null;

	}

	@Override
	public int hashCode() {
		int result = min != null ? min.hashCode() : 0;
		result = 31 * result + (max != null ? max.hashCode() : 0);
		return result;
	}
}
