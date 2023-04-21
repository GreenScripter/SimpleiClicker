package greenscripter.iclicker.api.data.response;

import java.util.List;

/*
 * GET
 * https://api.iclicker.com/trogon/v3/course/attendance/info/<attendence id>
 * {
 * "id": "0",
 * "checkedInCount": 0,
 * "notAttendingCount": 0,
 * "notCheckedInCount": 1,
 * "startDate": 1681160014482,
 * "endDate": null,
 * "timeElapsedMs": 1986,
 * "active": true,
 * "geoLocationSuspended": true,
 * "participants": [{
 * "userId": "e4",
 * "firstName": "",
 * "lastName": "",
 * "nonRegisteredClickerId": null,
 * "checkInDate": null,
 * "attendanceStatus": "-"
 * }]
 * }
 */
public class AttendanceResponse {

	String id;
	int checkedInCount;
	int notAttendingCount;
	int notCheckedInCount;
	long startDate;
	long endDate;
	long timeElapsedMs;
	boolean active;
	boolean geoLocationSuspended;
	List<Participant> participants;

	public static class Participant {

		String userId;
		String firstName;
		String lastName;
		Object nonRegisteredClickerId;
		Object checkInDate;
		String attendanceStatus;
	}
}
