package greenscripter.iclicker.api.data.response;

import java.util.List;

/*
 * GET https://api.iclicker.com/v1/courses/<course id>
 * {
 * "courseId": "2c",
 * "institutionId": "ab",
 * "name": "i clicker test course",
 * "courseDetails": {
 * "productLine": "CLOUD",
 * "courseType": "FULL_COURSE",
 * "joinCode": "JDTG",
 * "disciplineId": "9f",
 * "start": "2023-04-10T07:00:00Z",
 * "end": "2023-10-11T06:59:59Z",
 * "listed": "2023-04-10T20:35:11.621Z",
 * "courseIdentifier": null,
 * "term": null,
 * "meetingTimes": [],
 * "archived": null,
 * "requiresSubscription": false
 * },
 * "deviceSettings": {
 * "deviceTypes": "DEVICES_ONLY",
 * "enableFocus": false,
 * "baseStationFrequency1": "A",
 * "baseStationFrequency2": "A"
 * },
 * "attendanceSettings": {
 * "enableAttendance": true,
 * "highlightUnexcusedAbsences": false,
 * "unexcusedAbsenceThreshold": 3,
 * "enableAutoRun": false,
 * "timeZone": "America/Los_Angeles",
 * "attendanceTimes": [],
 * "requireLocation": false,
 * "location": null,
 * "radiusInMeters": 45.72,
 * "allowRemoteCheckin": true
 * },
 * "pollSettings": {
 * "shareScreenOption": "WHEN_QUESTION_STARTS",
 * "shareResultsOption": "WHEN_QUESTION_ENDS",
 * "participationPoints": 1.0,
 * "participationThreshold": "PERCENT_75_OF_QUESTIONS",
 * "performanceScoringType": "QUESTION_BASED_SCORING",
 * "answerPoints": 0.0,
 * "correctAnswerPoints": 1.0,
 * "pointsPerActivity": 5.0,
 * "timerType": "COUNT_UP",
 * "timerInSeconds": 30
 * },
 * "quizSettings": {
 * "shareResults": false,
 * "correctAnswerPoints": 1.0,
 * "answerPoints": 0.0
 * },
 * "groupSettings": null,
 * "integrationSettings": {
 * "coursewareId": null,
 * "gradeSyncType": "NONE",
 * "gradeSyncScoringType": "SYNC_TOTAL_SCORE",
 * "lmsCourseIdentifier": null,
 * "lmsCourseName": null,
 * "lmsSections": [],
 * "lmsLtiSettings": null,
 * "lmsRestSettings": null,
 * "lastRosterSyncFinished": null,
 * "lastGradeSyncFinished": null
 * },
 * "courseAggregates": {
 * "totalParticipationPoints": 0.0,
 * "averageParticipationPoints": 0.0,
 * "totalPerformancePoints": 0.0,
 * "averagePerformancePoints": 0.0
 * },
 * "created": "2023-04-10T20:32:07.394Z",
 * "creator": "51",
 * "updated": "2023-04-10T20:47:40.829Z",
 * "updater": "51",
 * "deleted": null,
 * "deleter": null
 * }
 */
public class CourseInfoResponse {

	String courseId;
	String institutionId;
	String name;
	CourseDetails courseDetails;
	DeviceSettings deviceSettings;
	AttendanceSettings attendanceSettings;
	PollSettings pollSettings;
	QuizSettings quizSettings;
	Object groupSettings;
	Object integrationSettings;
	CourseAggregates courseAggregates;
	String created;
	String creator;
	String updated;
	String updater;
	String deleted;
	String deleter;

	public static class CourseDetails {

		String productLine;
		String courseType;
		String joinCode;
		String disciplineId;
		String start;
		String end;
		String listed;
		String courseIdentifier;
		Object term;
		List<Object> meetingTimes;
		Object archived;
		boolean requiresSubscription;
	}

	public static class DeviceSettings {

		String deviceTypes;
		boolean enableFocus;
		String baseStationFrequency1;
		String baseStationFrequency2;
	}

	public static class AttendanceSettings {

		boolean enableAttendance;
		boolean highlightUnexcusedAbsences;
		int unexcusedAbsenceThreshold;
		boolean enableAutoRun;
		String timeZone;
		List<Object> attendanceTimes;
		boolean requireLocation;
		Object location;
		double radiusInMeters;
		boolean allowRemoteCheckin;
	}

	public static class PollSettings {

		String shareScreenOption;
		String shareResultsOption;
		double participationPoints;
		String participationThreshold;
		String performanceScoringType;
		double answerPoints;
		double correctAnswerPoints;
		double pointsPerActivity;
		String timerType;
		double timerInSeconds;
	}

	public static class QuizSettings {

		boolean shareResults;
		double correctAnswerPoints;
		double answerPoints;
	}

	public static class CourseAggregates {

		double totalParticipationPoints;
		double averageParticipationPoints;
		double totalPerformancePoints;
		double averagePerformancePoints;
	}
}
