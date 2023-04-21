package greenscripter.iclicker.api;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import greenscripter.iclicker.api.data.response.ActiveClassResponse;
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

public class IClickerInstance {

	public String email;
	public String password;
	public String refreshToken;
	public String token;
	public long expires;
	public boolean remember;

	public transient IClickerSession session = new IClickerSession();
	public transient IClickerAPI api = new IClickerAPI();

	/**
	 * Login with username and password, auto login not properly implemented.
	 */
	public void login() throws IOException {
		if (expired()) {
			LoginResponse r = null;
			//			if (token == null) {
			r = api.login(email, password);
			//			} else {
			//				api.token = token;
			//				r = api.autoLogin();
			//			}
			token = r.access_token();
			refreshToken = r.refresh_token();
			expires = System.currentTimeMillis() + r.expires_in() * 1000;
		}
		api.token = token;

		ProfileResponse profile = api.getProfile();

		session.discipline = profile.discipline;
		session.firstName = profile.firstName;
		session.lastName = profile.lastName;
		session.institutionName = profile.institutionName;
		session.userId = profile.userid;
		session.secretKey = profile.seckey;

		api.secretKey = session.secretKey;
		api.userid = session.userId;

	}

	public List<String> getActiveClassIds() throws IOException {
		ActiveClassResponse resp = api.getActiveClasses(session.userId);
		List<String> ids = new ArrayList<>();
		ids.addAll(resp.activeClassCourseIdList);
		return ids;
	}

	public boolean expired() {
		return System.currentTimeMillis() - expires > 0;
	}

	public CoursesResponse getCourses() throws IOException {
		return api.getCourses(session.userId);
	}

	public String startClass(String uuid) throws IOException {
		return api.startClass(uuid).classSessionId();
	}

	public void stopClass(String uuid) throws IOException {
		api.stopClass(uuid);
	}

	public StartActivityResponse startPoll(String uuid, String meetingId) throws IOException {
		return api.startActivity(uuid, meetingId);
	}

	public PollQuestionResponse startPollQuestion(String activityId, BufferedImage screenshot) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(screenshot, "png", out);
		String image = Base64.getEncoder().encodeToString(out.toByteArray());
		return api.createPollQuestion(activityId, image);
	}

	public EndActivityResponse endPoll(String activityId) throws IOException {
		return api.endActivity(activityId);
	}

	public EndQuestionResponse endPollQuestion(String questionId) throws IOException {
		return api.endQuestion(questionId);
	}
	
	public ClassStatusResponse getClassStatus(String classId) throws IOException {
		return api.getClassStatus(classId);
	}

	public QuestionStatusResponse getQuestionResults(String classId, String activityId, String questionId) throws IOException {
		return api.getQuestionStatus(classId, activityId, questionId);
	}

	public QuestionAnswerResponse setAnswer(String activityId, String questionId, String name, boolean correct) throws IOException {
		return api.setQuestionAnswer(activityId, questionId, name, correct);
	}

}
