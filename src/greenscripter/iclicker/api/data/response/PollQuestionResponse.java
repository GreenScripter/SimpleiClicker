package greenscripter.iclicker.api.data.response;

/*
 * {
 * "questionId": "f2887290-e21b-4ee4-80b6-ee28441d8442",
 * "anonymous": false,
 * "answerType": "SINGLE_ANSWER",
 * "enableConfidenceRating": false,
 * "graded": false,
 * "started": "2023-04-10T20:54:01.033Z",
 * "activityId": "fe8a0848-0485-4c03-9907-c55f3dd2d5a8",
 * "name": "Question 1",
 * "attachmentId": "03d91db5-8365-4d59-8199-12b807ede549",
 * "created": "2023-04-10T20:54:01.607Z",
 * "updated": "2023-04-10T20:54:01.607Z",
 * "imageUrl":
 * "https://&Expires=1681163641",
 * "largeThumbnailImageUrl":
 * "https://ires=1681163641",
 * "smallThumbnailImageUrl":
 * "https:&Expires=1681163641"
 * }
 */
public class PollQuestionResponse {

	public String questionId;
	public boolean anonymous;
	public String answerType;
	public boolean enableConfidenceRating;
	public boolean graded;
	public String started;
	public String activityId;
	public String name;
	public String attachmentId;
	public String created;
	public String updated;
	public String imageUrl;
	public String largeThumbnailImageUrl;
	public String smallThumbnailImageUrl;
}
