package greenscripter.iclicker.api.data.response;

/*
 * {
 * "activityId": "f8",
 * "activityType": "POLL",
 * "answerPoints": 0.0,
 * "correctAnswerPoints": 1.0,
 * "courseId": "2c",
 * "started": "2023-04-10T20:53:59.981Z",
 * "meetingId": "5b",
 * "name": "Class 3 - Poll",
 * "pollSettings": {
 * "shareQuestionImages": true,
 * "shareResults": true,
 * "participationPoints": 1.0,
 * "participationThreshold": "PERCENT_75_OF_QUESTIONS",
 * "performanceScoringType": "QUESTION_BASED_SCORING",
 * "pointsPerActivity": 5.0
 * },
 * "created": "2023-04-10T20:53:59.983Z",
 * "updated": "2023-04-10T20:53:59.983Z"
 * }
 */
public class StartActivityResponse {

	public String activityId;
	public String activityType;
	public double answerPoints;
	public double correctAnswerPoints;
	public String courseId;
	public String started;
	public String meetingId;
	public String name;
	public PollSettings pollSettings;
	public String created;
	public String updated;

	static class PollSettings {

		boolean shareQuestionImages;
		boolean shareResults;
		double participationPoints;
		String participationThreshold;
		String performanceScoringType;
		String pointsPerActivity;
	}
}
