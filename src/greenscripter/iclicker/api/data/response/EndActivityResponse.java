package greenscripter.iclicker.api.data.response;

/*
 * {
 * "activityId": "4dc90ef4-df19-40fe-8f9c-a299bff4e971",
 * "activityType": "POLL",
 * "answerPoints": 0.0,
 * "correctAnswerPoints": 1.0,
 * "courseId": "2c",
 * "started": "2023-04-13T04:55:29.438Z",
 * "meetingId": "5559dff8-61d6-4e61-a2df-4cafe614fc8d",
 * "name": "Class 5 - Poll",
 * "pollSettings": {
 * "shareQuestionImages": true,
 * "shareResults": true,
 * "participationPoints": 1.0,
 * "participationThreshold": "PERCENT_75_OF_QUESTIONS",
 * "performanceScoringType": "QUESTION_BASED_SCORING",
 * "pointsPerActivity": 5.0
 * },
 * "created": "2023-04-13T04:55:29.441Z",
 * "updated": "2023-04-13T04:56:35.042Z",
 * "activityAggregates": {
 * "totalQuestions": 2,
 * "averageParticipationPoints": 1.0,
 * "averagePerformancePoints": 1.0,
 * "totalParticipationPoints": 1.0,
 * "totalPerformancePoints": 2.0,
 * "totalSubmitted": 1,
 * "totalUsers": 1
 * },
 * "graded": true,
 * "ended": "2023-04-13T04:56:35.041Z"
 * }
 */
public class EndActivityResponse {

	public String activityId;
	public String activityType;
	public double answerPoints;
	public double correctAnswerPoints;
	public String courseId;
	public String started;
	public String meetingId;
	public String name;
	public Object pollSettings;
	public String created;
	public String updated;
	public Object activityAggregates;
	public boolean graded;
	public String ended;
}
