package edu.utexas.shared;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;

public class SimpleDate {

    public static final String[] MONTHS = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private int month;
    private int day;
    private int year;

    public SimpleDate(int month, int day, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public SimpleDate() {
        day = 1;
        month = 1;
        year = 2016;
    }

    @JsonIgnore
    public LocalDate getAsLocalDate() {
        return LocalDate.of(year, month, day);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    @JsonIgnore
    public String getMonthAsString() {
        return MONTHS[month - 1];
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "SimpleDate{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleDate date = (SimpleDate) o;

        if (day != date.day) return false;
        if (month != date.month) return false;
        return year == date.year;

    }

    @Override
    public int hashCode() {
        int result = (int) day;
        result = 31 * result + (int) month;
        result = 31 * result + year;
        return result;
    }
}
