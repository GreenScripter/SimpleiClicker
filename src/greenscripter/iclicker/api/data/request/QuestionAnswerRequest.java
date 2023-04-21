package greenscripter.iclicker.api.data.request;

/*
 * POST https://api.iclicker.com/v2/activities/<activity id>/questions/<question id>/results
 * {
 * "activityId": "",
 * "answer": "c",
 * "best": false,
 * "correct": true,
 * "instructorAnswer": false,
 * "questionId": ""
 * }
 */
public record QuestionAnswerRequest(String activityId, String answer, boolean best, boolean correct, boolean instructorAnswer, String questionId) {

}
