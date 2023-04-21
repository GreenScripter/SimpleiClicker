package greenscripter.iclicker.api.data.request;

/*
 * POST https://api.iclicker.com/activity/trogon/class/stop
 * {"courseId":"2c", "createExitPoll":false}
 */
public record ClassStopRequest(String courseId, boolean createExitPoll) {

}
