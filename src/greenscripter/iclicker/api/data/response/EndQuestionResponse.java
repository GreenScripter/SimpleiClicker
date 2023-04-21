package greenscripter.iclicker.api.data.response;

/*
 * {
 * "questionId": "f2",
 * "anonymous": false,
 * "answerType": "SINGLE_ANSWER",
 * "enableConfidenceRating": false,
 * "graded": false,
 * "started": "2023-04-10T20:54:01.033Z",
 * "activityId": "f8",
 * "name": "Question 1",
 * "attachmentId": "09",
 * "created": "2023-04-10T20:54:01.607Z",
 * "updated": "2023-04-10T20:54:09.474Z",
 * "ended": "2023-04-10T20:54:09.474Z"
 * }
 */
public class EndQuestionResponse {

	String questionId;
	boolean anonymous;
	String answerType;
	boolean enableConfidenceRating;
	boolean graded;
	String started;
	String activityId;
	String name;
	String attachmentId;
	String created;
	String updated;
	String ended;
}
