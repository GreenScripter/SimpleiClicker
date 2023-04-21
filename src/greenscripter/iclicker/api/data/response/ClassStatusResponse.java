package greenscripter.iclicker.api.data.response;

/*
 * GET https://api.iclicker.com/activity/class/status/<class id>
 * {
 * "startDate": "2023-04-10T20:53:34.348+00:00",
 * "courseId": "c",
 * "attendanceSessionId": "0c",
 * "classSessionId": "5b",
 * "classSessionActive": true
 * }
 */
public class ClassStatusResponse {

	public String startDate;
	public String courseId;
	public String attendanceSessionId;
	public String classSessionId;
	public boolean classSessionActive;
}
