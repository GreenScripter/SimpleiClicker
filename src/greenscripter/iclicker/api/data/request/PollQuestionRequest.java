package greenscripter.iclicker.api.data.request;

/*
 * POST https://api.iclicker.com/v2/activities/<activity id>/questions;
 * {"anonymous":false,"answerType":"SINGLE_ANSWER","attachment":"<base 64 encoded jpg>"
 * ,"enableConfidenceRating":false,
 * "graded":false,"started":"2023-04-10T20:54:00.123Z"}
 */
public record PollQuestionRequest(boolean anonymous, String answerType, String attachment, boolean enableConfidenceRating, boolean graded, String started) {
}
