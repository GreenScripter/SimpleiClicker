package greenscripter.iclicker.api.data.request;

/*
 * POST https://api.iclicker.com/v2/courses/<class id>/activities
 * {
 * "activityType": "POLL",
 * "answerPoints": 0,
 * "correctAnswerPoints": 1,
 * "courseId": "2c",
 * "meetingId": "5b",
 * "started": "2023-04-10T20:53:59.233Z"
 * }
 */
public record StartActivityRequest(String activityType, int answerPoints, int correctAnswerPoints, String courseId, String meetingId, String started) {

}
