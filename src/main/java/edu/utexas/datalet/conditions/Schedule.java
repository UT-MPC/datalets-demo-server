package edu.utexas.datalet.conditions;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.utexas.shared.SimpleDate;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Schedule extends Condition {
	private static final Logger log = LoggerFactory.getLogger(Schedule.class);

	public static final DateTimeFormatter FULL_FORMAT = DateTimeFormat.forPattern("MM/dd/yyyy h:mm a ZZZ");
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("MM/dd/yyyy");
	public static final DateTimeComparator DATE_ONLY = DateTimeComparator.getDateOnlyInstance();
	public static final DateTimeComparator TIME_ONLY = DateTimeComparator.getTimeOnlyInstance();
	public static final String DAILY = "daily";
	public static final String WEEKLY = "weekly";
	public static final String MONTHLY = "monthly";

	@JsonProperty("time")
	private String time;
	@JsonProperty("begins")
	private SimpleDate begins;
	@JsonProperty("repeats")
	private boolean repeats;
	@JsonProperty("ends")
	private SimpleDate ends;
	@JsonProperty("frequency")
	private String frequency;
	@JsonProperty("days")
	private List<String> days;

	public Schedule() {

	}

	public Schedule(String form, String operator, String operand, String time, SimpleDate begins, boolean repeats) {
		super(form, operator, operand);
		this.time = time;
		this.begins = begins;
		this.repeats = repeats;
	}

	@JsonIgnore
	@Override
	boolean isSubTypeValid() {
		if (time == null || begins == null) {
			return false;
		} if (repeats) {
			if (DAILY.equals(frequency)) {
				// DO NOTHING
			} else if (WEEKLY.equals(frequency)) {
				if (days == null || days.isEmpty()) {
					return false;
				}
			} else if (MONTHLY.equals(frequency)) {
				if (days == null || days.isEmpty()) {
					return false;
				}
			} else {
				return false;
			}
		}

		return true;
	}

	public boolean evaluateSchedule() {
		String date = String.format("%02d/%02d/%04d %s America/Chicago", begins.getMonth(), begins.getDay(), begins.getYear(), time);
		try {
			DateTime beginDate = FULL_FORMAT.parseDateTime(date);
			DateTime currentDate = new DateTime();
			currentDate = currentDate.withZone(DateTimeZone.forID("America/Chicago"));
			log.error("Begin: " + beginDate + ", Current: " + currentDate + ", Repeats: " + repeats);
			if (repeats) {
				log.error("Repeats - Ends: " + ends + ", Freq: " + frequency + ", Days: " + days);
				if (ends != null) {
					String endString = String.format("%02d/%02d/%04d", ends.getMonth(), ends.getDay(), ends.getYear());
					DateTime endDate = DATE_FORMAT.parseDateTime(endString);
					log.error("Current Date: " + currentDate + ", End Date: " + endDate);
					if (DATE_ONLY.compare(currentDate, endDate) > 0) {
						return false;
					}
				}

				if (DAILY.equals(frequency)) {
					// DO NOTHING
				} else if (WEEKLY.equals(frequency)) {
					int currentDayOfWeek = currentDate.getDayOfWeek();

					if (days == null || days.isEmpty()) {
						int beginDayOfWeek = beginDate.getDayOfWeek();
						if (beginDayOfWeek != currentDayOfWeek) {
							return false;
						}
					} else if (!days.contains(Integer.toString(currentDayOfWeek))) {
						return false;
					}

				} else if (MONTHLY.equals(frequency)) {
					int currentDayOfMonth = currentDate.getDayOfMonth();

					if (days == null || days.isEmpty()) {
						int beginDayOfMonth = beginDate.getDayOfMonth();
						if (beginDayOfMonth != currentDayOfMonth) {
							return false;
						}
					} else if (!days.contains(Integer.toString(currentDayOfMonth))) {
						return false;
					}
				}
			} else if (DATE_ONLY.compare(beginDate, currentDate) != 0) {
				return false;
			}

			return checkTime(beginDate, currentDate);

		} catch (Exception ex) {
			log.error("Exception evaluating schedule", ex);
			return false;
		}
	}

	private boolean checkTime(DateTime begin, DateTime current) {
		LocalTime beginLocal = begin.toLocalTime();
		LocalTime currentLocal = current.toLocalTime();


		Minutes minutes = Minutes.minutesBetween(beginLocal, currentLocal);

		if ("<".equals(operator)) {
			return !minutes.isGreaterThan(Minutes.minutes(Integer.parseInt(operand)));
		} else if (">".equals(operator)) {
			return minutes.isGreaterThan(Minutes.minutes(Integer.parseInt(operand)));
		} else {
			return false;
		}
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public SimpleDate getBegins() {
		return begins;
	}

	public void setBegins(SimpleDate begins) {
		this.begins = begins;
	}

	public boolean isRepeats() {
		return repeats;
	}

	public void setRepeats(boolean repeats) {
		this.repeats = repeats;
	}

	public SimpleDate getEnds() {
		return ends;
	}

	public void setEnds(SimpleDate ends) {
		this.ends = ends;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public List<String> getDays() {
		return days;
	}

	public void setDays(List<String> days) {
		this.days = days;
	}

	@Override
	public String toString() {
		return "Schedule{" + super.toString() +
				"time='" + time + '\'' +
				", begins='" + begins + '\'' +
				", repeats=" + repeats +
				", ends='" + ends + '\'' +
				", frequency='" + frequency + '\'' +
				", days=" + days +
				'}';
	}
}
