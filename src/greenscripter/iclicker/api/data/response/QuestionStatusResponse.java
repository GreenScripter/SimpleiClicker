package greenscripter.iclicker.api.data.response;

import java.util.List;

/*
 * GET https://api.iclicker.com/v2/reporting/courses/<course id>/activities/<activity
 * id>/questions/view?questionId=<question id>
 * {
 * "totalResponses": 0,
 * "questions": [{
 * "questionId": "f2",
 * "anonymous": false,
 * "answerType": "SINGLE_ANSWER",
 * "enableConfidenceRating": false,
 * "graded": false,
 * "name": "Question 1",
 * "created": "2023-04-10T20:54:01.607Z",
 * "results": [],
 * "responseCount": 0,
 * "answerOverview": [],
 * "imageUrl":""
 * "largeThumbnailImageUrl": "",
 * "smallThumbnailImageUrl": ""
 * }]
 * }
 * {
 * "totalResponses": 1,
 * "questions": [{
 * "questionId": "f2887290-e21b-4ee4-80b6-ee28441d8442",
 * "anonymous": false,
 * "answerType": "SINGLE_ANSWER",
 * "enableConfidenceRating": false,
 * "graded": false,
 * "name": "Question 1",
 * "created": "2023-04-10T20:54:01.607Z",
 * "totalPossibleGradedPoints": 0.0,
 * "results": [{
 * "resultId": "b32faf14-9de0-4b71-99fb-958453829cdd",
 * "questionId": "f2887290-e21b-4ee4-80b6-ee28441d8442",
 * "answer": "B",
 * "pointsOverridden": false,
 * "performancePoints": 0.0,
 * "reportingAggregates": [{
 * "count": 1,
 * "percentageOfTotalResponses": 100.0
 * }],
 * "correct": false
 * }],
 * "responseCount": 1,
 * "answerOverview": [{
 * "answer": "B",
 * "correct": false,
 * "count": 1,
 * "percentageOfTotalResponses": 100.0
 * }],
 * "answerBuckets": {
 * "allAnswers": [{
 * "answer": "B",
 * "correct": false,
 * "count": 1,
 * "percentageOfTotalResponses": 100.0,
 * "resultId": "b32faf14-9de0-4b71-99fb-958453829cdd"
 * }]
 * },
 * "imageUrl": "",
 * "largeThumbnailImageUrl": "",
 * "smallThumbnailImageUrl": ""
 * }]
 * }
 */
public class QuestionStatusResponse {

	public int totalResponses;
	public List<Question> questions;

	public static class Question {

		public String questionId;
		public boolean anonymous;
		public String answerType;
		public boolean enableConfidenceRating;
		public double totalPossibleGradedPoints;
		public boolean graded;
		public String name;
		public String created;
		public List<Result> results;
		public int responseCount;
		public List<AnswerOverview> answerOverview;
		public Object answerBuckets;
		public String imageUrl;
		public String largeThumbnailImageUrl;
		public String smallThumbnailImageUrl;

		public static record AnswerOverview(String answer, boolean correct, int count, double percentageOfTotalResponses) {

		}

		public static class Result {

			public String resultId;
			public String questionId;
			public String answer;
			public boolean pointsOverridden;
			double performancePoints;
			public List<ReportingAggregate> reportingAggregates;
			public boolean correct;

			public static record ReportingAggregate(int count, double percentageOfTotalResponses) {

			}
		}
	}
}
