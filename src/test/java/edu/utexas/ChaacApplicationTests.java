package edu.utexas;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utexas.datalet.Criteria;
import edu.utexas.datalet.Datalet;
import edu.utexas.datalet.conditions.Condition;
import edu.utexas.datalet.conditions.Location;
import edu.utexas.datalet.conditions.Schedule;
import edu.utexas.shared.GPSPoint;
import edu.utexas.shared.SimpleDate;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChaacApplicationTests {

	@Test
	public void contextLoads() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


		Condition myLocation = new Location("location", "50", "<", new GPSPoint(37.012d, 97.12d));
		Condition mySchedule = new Schedule("schedule", "60", "<", "11:59 PM", new SimpleDate(11, 15, 2016), false);

		try {
			String locationJson = objectMapper.writeValueAsString(myLocation);
			System.out.println(locationJson);
			Condition deserializedLocation = objectMapper.readValue(locationJson, Condition.class);
			System.out.println("Deserialized Location Class: " + deserializedLocation.getClass().getSimpleName());

			String testJson = "{\"availability\":{\"children\":[{\"condition\":{\"form\":\"location\",\"operand\":\"100\",\"operator\":\"\\u003c\"},\"type\":\"condition\"},{\"condition\":{\"attribute\":\"firstName\",\"form\":\"profile\",\"operand\":\"test\",\"operator\":\"\\u003d\"},\"type\":\"condition\"}],\"type\":\"and\"},\"data\":{\"test\":\"value\"},\"description\":\"Let\\u0027s create another one\",\"id\":\"58242fd46db266ae82ee3c6b\",\"location\":{\"latitude\":30.28685557476789,\"longitude\":-97.73659888654947,\"mVersionCode\":0},\"ownerID\":\"58239d6e6db2666ecc946de3\",\"precondition\":{\"children\":[{\"condition\":{\"begins\":{\"day\":14,\"month\":11,\"year\":2016},\"repeats\":false,\"time\":\"1:48 AM\",\"form\":\"schedule\",\"operand\":\"60\",\"operator\":\"\\u003c\"},\"type\":\"condition\"},{\"condition\":{\"point\":{\"latitude\":30.28685557476789,\"longitude\":-97.73659888654947,\"mVersionCode\":1},\"form\":\"location\",\"operand\":\"100\",\"operator\":\"\\u003c\"},\"type\":\"condition\"}],\"type\":\"and\"},\"title\":\"Another\",\"useOwnerLocation\":true}";
			Datalet testSer = objectMapper.readValue(testJson, Datalet.class);
			System.out.println("\nDeserialized Location: " + testSer);

			System.out.println("Serialized: " + objectMapper.writeValueAsString(testSer));

			String testPre = "{\"children\":[{\"condition\":{\"begins\":{\"day\":14,\"month\":11,\"year\":2016},\"repeats\":false,\"time\":\"1:48 AM\",\"form\":\"schedule\",\"operand\":\"60\",\"operator\":\"\\u003c\"},\"type\":\"condition\"},{\"condition\":{\"point\":{\"latitude\":30.28685557476789,\"longitude\":-97.73659888654947,\"mVersionCode\":1},\"form\":\"location\",\"operand\":\"100\",\"operator\":\"\\u003c\"},\"type\":\"condition\"}],\"type\":\"and\"}";
			Criteria precondition = objectMapper.readValue(testPre, Criteria.class);
			System.out.println("\nPrecondition: " + precondition);
			System.out.println("Serialized: " + objectMapper.writeValueAsString(precondition));
//
			String testCri = "{\"condition\":{\"begins\":{\"day\":14,\"month\":11,\"year\":2016},\"repeats\":false,\"time\":\"1:48 AM\",\"form\":\"schedule\",\"operand\":\"60\",\"operator\":\"\\u003c\"},\"type\":\"condition\"}";
			Criteria criteria = objectMapper.readValue(testCri, Criteria.class);
			System.out.println("\nCriteria: " + criteria);
			System.out.println("Condition: " + objectMapper.writeValueAsString(criteria.getCondition()));
			System.out.println("Serialized: " + objectMapper.writeValueAsString(criteria));

			String testCon = "{\"begins\":{\"day\":14,\"month\":11,\"year\":2016},\"repeats\":false,\"time\":\"1:48 AM\",\"form\":\"schedule\",\"operand\":\"60\",\"operator\":\"\\u003c\"}";
			Condition condition = objectMapper.readValue(testCon, Condition.class);
			System.out.println("\nCondition: " + condition);
			System.out.println("Serialized: " + objectMapper.writeValueAsString(condition));


			String scheduleJson = objectMapper.writeValueAsString(mySchedule);
			System.out.println("\n" + scheduleJson);
			Condition deserializedSchedule = objectMapper.readValue(scheduleJson, Condition.class);
			System.out.println("Deserialized Schedule Class: " + deserializedSchedule.getClass().getSimpleName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void testDistance() {
		GPSPoint point3 = new GPSPoint(30.430105, -97.696135);
		GPSPoint point4 = new GPSPoint(30.429340, -97.692104);

		double distance = point3.distance(point4);

		assertEquals(394.31, distance, 3.0);
	}

}
