package greenscripter.iclicker.api.data.response;

import java.util.ArrayList;
import java.util.List;

/*
 * https://api.iclicker.com/v1/users/<user id>/views/instructor-courses
 * [{
	"enrollmentId": "5c",
	"courseId": "2bc",
	"courseRoleType": "COURSE_OWNER",
	"name": "i clicker test course",
	"courseIdentifier": null,
	"joinCode": "JG",
	"term": null,
	"start": "2023-04-10T07:00:00Z",
	"end": "2023-10-11T06:59:59Z",
	"meetingTimes": [],
	"archived": null // or string date
}]
//manual list type
 */
public class CoursesResponse {

	public List<Course> courses = new ArrayList<>();
	public static class Course {
		public String enrollmentId;
		public String courseId;
		public String courseRoleType;
		public String name;
		public String courseIdentifier;
		public String joinCode;
		public Object term;
		public String start;
		public String end;
		public List<Object> meetingTimes;
		public String archived;
	}
}
