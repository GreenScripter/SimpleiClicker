package greenscripter.iclicker.api;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;

import greenscripter.iclicker.api.data.request.ActiveClassRequest;
import greenscripter.iclicker.api.data.request.AutoLoginRequest;
import greenscripter.iclicker.api.data.request.ClassStartRequest;
import greenscripter.iclicker.api.data.request.ClassStopRequest;
import greenscripter.iclicker.api.data.request.EndActivityRequest;
import greenscripter.iclicker.api.data.request.EndQuestionRequest;
import greenscripter.iclicker.api.data.request.LoginRequest;
import greenscripter.iclicker.api.data.request.PollQuestionRequest;
import greenscripter.iclicker.api.data.request.QuestionAnswerRequest;
import greenscripter.iclicker.api.data.request.StartActivityRequest;
import greenscripter.iclicker.api.data.response.ActiveClassResponse;
import greenscripter.iclicker.api.data.response.ClassStartResponse;
import greenscripter.iclicker.api.data.response.ClassStatusResponse;
import greenscripter.iclicker.api.data.response.CoursesResponse;
import greenscripter.iclicker.api.data.response.EndActivityResponse;
import greenscripter.iclicker.api.data.response.EndQuestionResponse;
import greenscripter.iclicker.api.data.response.LoginResponse;
import greenscripter.iclicker.api.data.response.PollQuestionResponse;
import greenscripter.iclicker.api.data.response.ProfileResponse;
import greenscripter.iclicker.api.data.response.QuestionAnswerResponse;
import greenscripter.iclicker.api.data.response.QuestionStatusResponse;
import greenscripter.iclicker.api.data.response.StartActivityResponse;
import greenscripter.utils.http.HTTP;
import greenscripter.utils.http.Request;
import greenscripter.utils.http.Response;

public class IClickerAPI {

	public String token;
	public String secretKey;
	public String userid;
	static Gson gson = new Gson();

	/**
	 * Set the correct answer to a poll question, marking it as correct, not best and not
	 * instructorAnswer.
	 */
	public QuestionAnswerResponse setQuestionAnswer(String activityId, String questionId, String answer, boolean correct) throws IOException {
		Request req = addBearer(createWebRequest("POST"));
		req.headers.add("content-type", "application/json");
		req.setContent(gson.toJson(new QuestionAnswerRequest(activityId, answer, false, correct, false, questionId)).getBytes());
		Response r = HTTP.sendRequest("https://api.iclicker.com/v2/activities/" + activityId + "/questions/" + questionId + "/results", req);
		QuestionAnswerResponse resp = deserialize(r.body, QuestionAnswerResponse.class);
		return resp;
	}

	/**
	 * End an activity that is currently in progress.
	 */
	public EndActivityResponse endActivity(String activityId) throws IOException {
		Request req = addBearer(createWebRequest("POST"));
		req.headers.add("content-type", "application/json");
		req.setContent(gson.toJson(new EndActivityRequest()).getBytes());
		Response r = HTTP.sendRequest("https://api.iclicker.com/v2/activities/" + activityId + "/end-activity", req);
		EndActivityResponse resp = deserialize(r.body, EndActivityResponse.class);
		return resp;
	}

	/**
	 * End a question that is currently in progress.
	 */
	public EndQuestionResponse endQuestion(String questionId) throws IOException {
		Request req = addBearer(createWebRequest("POST"));
		req.headers.add("content-type", "application/json");
		req.setContent(gson.toJson(new EndQuestionRequest()).getBytes());
		Response r = HTTP.sendRequest("https://api.iclicker.com/v2/questions/" + questionId + "/end-question", req);
		EndQuestionResponse resp = deserialize(r.body, EndQuestionResponse.class);
		return resp;
	}

	/**
	 * Get the current status of a question that was asked. Called by the official client at regular
	 * intervals during a poll.
	 */
	public QuestionStatusResponse getQuestionStatus(String classId, String activityId, String questionId) throws IOException {
		Request req = addBearer(createWebRequest("GET"));

		Response r = HTTP.sendRequest("https://api.iclicker.com/v2/reporting/courses/" + classId + "/activities/" + activityId + "/questions/view?questionId=" + questionId, req);
		QuestionStatusResponse resp = deserialize(r.body, QuestionStatusResponse.class);
		return resp;
	}

	/**
	 * Start a SINGLE_ANSWER POLL question.
	 */
	public PollQuestionResponse createPollQuestion(String activityId, String base64Image) throws IOException {
		Request req = addBearer(createClientRequest("POST"));
		req.headers.add("content-type", "application/json");

		req.setContent(gson.toJson(new PollQuestionRequest(false, "SINGLE_ANSWER", base64Image, false, false, Instant.now().toString())).getBytes());

		Response r = HTTP.sendRequest("https://api.iclicker.com/v2/activities/" + activityId + "/questions", req);
		PollQuestionResponse resp = deserialize(r.body, PollQuestionResponse.class);
		return resp;
	}

	/**
	 * Start a POLL activity with default settings starting now for a class and meeting of that
	 * class.
	 */
	public StartActivityResponse startActivity(String classId, String meetingId) throws IOException {
		Request req = addBearer(createClientRequest("POST"));
		req.headers.add("content-type", "application/json");

		req.setContent(gson.toJson(new StartActivityRequest("POLL", 0, 1, classId, meetingId, Instant.now().toString())).getBytes());

		Response r = HTTP.sendRequest("https://api.iclicker.com/v2/courses/" + classId + "/activities", req);
		StartActivityResponse resp = deserialize(r.body, StartActivityResponse.class);
		return resp;
	}

	/**
	 * Get a list of active classes, regularly called by the normal client at all times.
	 */
	public ActiveClassResponse getActiveClasses(String userId) throws IOException {
		Request req = addBearer(createWebRequest("POST"));
		req.headers.add("content-type", "application/vnd.reef.class-active-list-request-v1+json");
		req.headers.remove("accept");
		req.headers.add("accept", "application/vnd.reef.class-active-list-response-v1+json");
		req.setContent(gson.toJson(new ActiveClassRequest(userId)).getBytes());
		Response r = HTTP.sendRequest("https://api.iclicker.com/activity/class/active/list", req);
		ActiveClassResponse resp = deserialize(r.body, ActiveClassResponse.class);
		return resp;
	}

	/**
	 * Get information on a class that is running, or a bunch of null fields if it isn't in
	 * progress.
	 */
	public ClassStatusResponse getClassStatus(String classId) throws IOException {
		Response r = HTTP.sendRequest("https://api.iclicker.com/activity/class/status/" + classId, addBearer(createWebRequest("GET")));
		ClassStatusResponse resp = deserialize(r.body, ClassStatusResponse.class);
		return resp;
	}

	/**
	 * Start a class
	 */
	public ClassStartResponse startClass(String classId) throws IOException {
		Request req = createClientRequest("POST").setContent(gson.toJson(new ClassStartRequest(classId)).getBytes());
		req.headers.add("content-type", "application/vnd.reef.class-start-request-v1+json");

		addTRGN(req);

		Response r = HTTP.sendRequest("https://api.iclicker.com/activity/trogon/class/start", req);
		ClassStartResponse resp = deserialize(r.body, ClassStartResponse.class);
		return resp;
	}

	/**
	 * Stop a class
	 */
	public ClassStartResponse stopClass(String classId) throws IOException {
		Request req = createClientRequest("POST").setContent(gson.toJson(new ClassStopRequest(classId, false)).getBytes());
		req.headers.add("content-type", "application/vnd.reef.class-stop-request-v1+json");

		addTRGN(req);

		Response r = HTTP.sendRequest("https://api.iclicker.com/activity/trogon/class/stop", req);
		ClassStartResponse resp = deserialize(r.body, ClassStartResponse.class);
		return resp;
	}

	/**
	 * Get course list.
	 */
	public CoursesResponse getCourses(String userId) throws IOException {
		Response r = HTTP.sendRequest("https://api.iclicker.com/v1/users/" + userId + "/views/instructor-courses", addBearer(createWebRequest("GET")));
		CoursesResponse.Course[] courses = deserialize(r.body, CoursesResponse.Course[].class);
		CoursesResponse resp = new CoursesResponse();
		resp.courses.addAll(List.of(courses));
		return resp;
	}

	/**
	 * Get the teacher profile.
	 */
	public ProfileResponse getProfile() throws IOException {
		Response r = HTTP.sendRequest("https://api.iclicker.com/trogon/v4/profile", addBearer(createWebRequest("GET")));
		return deserialize(r.body, ProfileResponse.class);
	}

	/**
	 * Login in using refresh token/saved data. WIP, not currently working.
	 */
	//TODO WIP
	public LoginResponse autoLogin() throws IOException {
		Request req = createWebRequest("POST").setContent(gson.toJson(new AutoLoginRequest(UUID.randomUUID().toString(), "reef-instructor-web-client-id")).getBytes());
		req.headers.add("content-type", "application/json");
		Response r = HTTP.sendRequest("https://api.iclicker.com/login/autologin", req);
		System.out.println(new String(req.getBytes()));
		r.disableCompression();
		System.out.println(new String(r.getBytes()));
		return deserialize(r.body, LoginResponse.class);
	}

	/**
	 * Log in with email and password
	 */
	public LoginResponse login(String email, String password) throws IOException {
		Request req = createWebRequest("POST").setContent(gson.toJson(new LoginRequest(email, password)).getBytes());
		req.headers.add("content-type", "application/vnd.reef.login-proxy-request-v1+json");
		Response r = HTTP.sendRequest("https://api.iclicker.com/authproxy/login", req);
		return deserialize(r.body, LoginResponse.class);
	}

	public <T> T deserialize(byte[] data, Class<T> type) {
		return gson.fromJson(new String(data), type);
	}

	/*
	 * https://api.iclicker.com/authproxy/login
	 * :method: POST
	 * :authority: api.iclicker.com
	 * :scheme: https
	 * :path: /authproxy/login
	 * content-length: 69
	 * origin: https://instructor.iclicker.com
	 * user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_16_0) AppleWebKit/537.36 (KHTML, like
	 * Gecko) QtWebEngine/5.15.1 Chrome/80.0.3987.163 Safari/537.36
	 * content-type: application/vnd.reef.login-proxy-request-v1+json
	 * reef-auth-type: oauth
	 * accept: application/json
	 * sec-fetch-dest: empty
	 * client-tag: REEF/INSTRUCTOR/5.0.5/WEB///
	 * sec-fetch-site: same-site
	 * sec-fetch-mode: cors
	 * referer: https://instructor.iclicker.com/
	 * accept-encoding: gzip, deflate, br
	 * {"email":"","password":""}
	 */
	public Request createWebRequest(String method) {
		Request r = new Request();
		r.method = method;
		r.headers.add("origin", "https://instructor.iclicker.com");
		r.headers.add("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_16_0) AppleWebKit/537.36 (KHTML, like Gecko) QtWebEngine/5.15.1 Chrome/80.0.3987.163 Safari/537.36");
		r.headers.add("accept", "application/json");
		r.headers.add("sec-fetch-dest", "empty");
		r.headers.add("client-tag", "REEF/INSTRUCTOR/5.0.5/WEB///");
		r.headers.add("sec-fetch-site", "same-site");
		r.headers.add("sec-fetch-mode", "cors");
		r.headers.add("referer", "https://instructor.iclicker.com/");
		r.headers.add("accept-encoding", "gzip");
		r.headers.add("accept-language", "en-US,*");
		return r;
	}
	
	public Request createClientRequest(String method) {
		Request r = new Request();
		r.method = method;
		r.headers.add("origin", "https://instructor.iclicker.com");
		r.headers.add("user-agent", "Mozilla/5.0");
		r.headers.add("accept", "application/json");
		r.headers.add("sec-fetch-dest", "empty");
		r.headers.add("client-tag", "REEF/INSTRUCTOR/5.3.1/MAC/22.3.0");
		r.headers.add("sec-fetch-site", "same-site");
		r.headers.add("sec-fetch-mode", "cors");
		r.headers.add("referer", "https://instructor.iclicker.com/");
		r.headers.add("accept-encoding", "gzip");
		r.headers.add("accept-language", "en-US,*");
		r.headers.add("REEF-Client-ID", "reef-instructor");
		//Add date
		DateTimeFormatter format = DateTimeFormatter.ofPattern("EEE, dd LLL yyyy HH:mm:ss zzz");
		r.headers.add("date", ZonedDateTime.now().withZoneSameInstant(ZoneId.of("GMT")).format(format));
		return r;
	}

	public Request addBearer(Request r) {
		if (token != null) {
			r.headers.add("reef-auth-type", "oauth");
			r.headers.add("authorization", "Bearer " + token);
		}
		return r;
	}

	/**
	 * Add the custom TRGN Authorization header to a request. Needs to be called after the request
	 * is constructed.
	 * SHA1 MAC of <http request type>,<something, always blank?>,<accept header>,<content length or
	 * missing>,<date header> signed with the seckey field of the account
	 * <p>
	 * POST,,application/vnd.reef.class-start-request-v1+json,51,Thu, 20 Apr 2023 01:43:12 GMT
	 * <p>
	 * TRGN XfsfRMBgRRCacTrDpFeO8Q==:uphBEyqk5Jl9ZZBnAE42VlRIX+A=
	 */
	public Request addTRGN(Request r) {
		try {
			

			//userid as base64 from hex
			String base64Id = Base64.getEncoder().encodeToString(hexToBytes(userid));

			//generate checksum text
			//<http request type>,<something, always blank?>,<accept header>,<content length or missing>,<date header>
			String magicText = r.method + "," //
					+ /*Unknown*/","//
					+ r.getHeader("content-type") + ","//
					+ (r.body == null ? "" : r.body.length) + ","//
					+ r.getHeader("date");
			//			System.out.println(magicText);

			//sign message with account secret key.
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(new SecretKeySpec(hexToBytes(secretKey), "HmacSHA1"));

			mac.update(magicText.getBytes());

			byte[] authCode = mac.doFinal();

			//code as base 64 part.
			String base64Checksum = Base64.getEncoder().encodeToString(authCode);

			//TRGN auth string
			String code = base64Id + ":" + base64Checksum;
			//			System.out.println(code);

			r.headers.add("authorization", "TRGN " + code);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return r;
	}

	public static byte[] hexToBytes(String s) {
		s = s.replace("-", "");

		BigInteger content = new BigInteger(s, 16);
		byte[] initial = content.toByteArray();
		byte[] bytes = new byte[s.length() / 2];
		//		System.out.println(s);
		//		System.out.println(Arrays.toString(initial));
		//		System.out.println(Arrays.toString(bytes));

		System.arraycopy(initial, Math.max(initial.length - bytes.length, 0), bytes, Math.max(bytes.length - initial.length, 0), bytes.length);

		return bytes;
	}

}
