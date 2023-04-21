package greenscripter.iclicker.api.data.response;

/*
 * {
 * "attendanceSessionId": "05da70b8-0649-4bf2-a7c9-8c4e2fcab9dc",
 * "classSessionId": "56e49acf-6111-4c82-83ef-92871c451ebb"
 * }
 */
public record ClassStartResponse(String attendanceSessionId, String classSessionId) {

}
