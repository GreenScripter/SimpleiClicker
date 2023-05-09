package greenscripter.iclicker.gui;

import java.util.ArrayList;
import java.util.List;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import greenscripter.iclicker.api.IClickerInstance;
import greenscripter.iclicker.api.data.response.CoursesResponse.Course;
import greenscripter.iclicker.api.data.response.QuestionStatusResponse;
import greenscripter.iclicker.gui.InPollWindow.PollQuestion;
import greenscripter.iclicker.gui.ViewResultsWindow.Category;
import greenscripter.utils.http.HTTP;

public class SimpleiClicker {

	IClickerInstance instance;
	ClassListWindow classListWindow;
	InClassWindow inClassWindow;
	InPollWindow inPollWindow;
	InQuestionWindow inQuestionWindow;
	ViewResultsWindow viewResultsWindow;

	List<Course> courses = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		if (args.length > 0 && args[0].equals("-v")) {
			HTTP.REQUEST_LOGGING = true;
			HTTP.RESPONSE_LOGGING = true;
		}
		IClickerInstance instance = null;
		try {
			File auth = new File("iclickerauth.json");
			InputStream in = new FileInputStream(auth);
			byte[] data = in.readAllBytes();
			in.close();
			instance = new Gson().fromJson(new String(data), IClickerInstance.class);

			instance.login();

		} catch (Exception e) {
			instance = new IClickerInstance();
			LoginWindow.login(instance);
		}

		new SimpleiClicker(instance);

	}

	public SimpleiClicker(IClickerInstance instance) {
		this.instance = instance;
		classListWindow = new ClassListWindow(this);
		classListWindow.name.setText(instance.session.firstName + " " + instance.session.lastName);
		classListWindow.setVisible(true);

		//populate classes
		try {
			this.courses = instance.getCourses().courses;
			for (Course c : courses) {
				if (c.archived == null) classListWindow.addClass(c.courseId, c.name);
			}
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getClass() + " " + e.getMessage(), "Course List Error", JOptionPane.WARNING_MESSAGE);
		}

		//update any classes that might be in progress from a crash or another client.
		//Loop every ten seconds like the official client.
		new Thread(() -> {
			try {
				while (true) {
					if (classListWindow.isVisible()) try {
						List<String> active = instance.getActiveClassIds();
						classListWindow.applyActiveClasses(active);
					} catch (IOException e) {
						e.printStackTrace();
					}
					Thread.sleep(10000);
				}
			} catch (InterruptedException e) {

			}
		}).start();
	}

	public void startClass(String classId) {
		try {
			var classStatus = instance.getClassStatus(classId);

			String meetingId = classStatus.classSessionActive ? classStatus.classSessionId : instance.startClass(classId);

			inClassWindow = new InClassWindow(this, classId, meetingId, courses.stream()//
					.filter(c -> c.courseId.equals(classId))//
					.map(c -> c.name)//
					.findAny()//
					.orElse("null"));

			inClassWindow.setVisible(true);
			classListWindow.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getClass() + " " + e.getMessage(), "Start Class Error", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void endClass(String uuid) {
		try {
			instance.stopClass(uuid);

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getClass() + " " + e.getMessage(), "Stop Class Error", JOptionPane.WARNING_MESSAGE);
		}
		inClassWindow.setVisible(false);
		inClassWindow = null;

		classListWindow.setVisible(true);

		classListWindow.classes.stream()//
				.filter(c -> c.uuid.equals(uuid))//
				.forEach(c -> c.setInProgress(false));
	}

	public void startPoll(String classId, String meetingId) {
		try {
			var poll = instance.startPoll(classId, meetingId);
			inClassWindow.setVisible(false);

			inPollWindow = new InPollWindow(this, poll.activityId, poll.name);
			inPollWindow.setLocation(inClassWindow.getLocation());

			inPollWindow.setVisible(true);

		} catch (IOException | AWTException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getClass() + " " + e.getMessage(), "Start Poll Error", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void endPoll(String activityId) {
		try {
			if (inPollWindow.anyQuestionStarted) instance.endPoll(activityId);

			inClassWindow.setVisible(true);

			inPollWindow.setVisible(false);
			inPollWindow = null;
			if (viewResultsWindow != null) {
				viewResultsWindow.setVisible(false);
				viewResultsWindow = null;
			}

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getClass() + " " + e.getMessage(), "End Poll Error", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void startQuestion(String activityId, BufferedImage screenshot) {
		try {
			var question = instance.startPollQuestion(activityId, screenshot);
			inPollWindow.setVisible(false);
			inPollWindow.questions.add(new PollQuestion(question.questionId, question.name));

			if (viewResultsWindow != null) viewResultsWindow.setButtons();

			inQuestionWindow = new InQuestionWindow(this, question.questionId, question.name);
			inQuestionWindow.setLocation(inPollWindow.getLocation());
			inQuestionWindow.setVisible(true);

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getClass() + " " + e.getMessage(), "Start Question Error", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void endQuestion(String questionId) {
		try {
			instance.endPollQuestion(questionId);

			inPollWindow.setVisible(true);

			inQuestionWindow.setVisible(false);
			inQuestionWindow = null;

			if (viewResultsWindow != null) {
				viewResultsWindow.questionIndex = viewResultsWindow.questions.size() - 1;
				viewResultsWindow.changeQuestionIndex();
				viewResultsWindow.updateResults();
			}

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getClass() + " " + e.getMessage(), "End Question Error", JOptionPane.WARNING_MESSAGE);
		}
	}

	public QuestionStatusResponse getQuestionResults(String classId, String activityId, String questionId) {
		try {
			return instance.getQuestionResults(classId, activityId, questionId);

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getClass() + " " + e.getMessage(), "Question Results Error", JOptionPane.WARNING_MESSAGE);
		}
		return null;
	}

	public void setAnswer(String activityId, String questionId, String name, boolean correct) {
		try {
			instance.setAnswer(activityId, questionId, name, correct);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getClass() + " " + e.getMessage(), "Set Answer Error", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void viewResults(String questionId, String name) {
		if (viewResultsWindow == null) {
			viewResultsWindow = new ViewResultsWindow(this, inClassWindow.classId, inPollWindow.activityId, questionId, name, inPollWindow.questions);
		} else {
			viewResultsWindow.questions = inPollWindow.questions;
			viewResultsWindow.classId = inClassWindow.classId;
			viewResultsWindow.activityId = viewResultsWindow.activityId;
			viewResultsWindow.questionId = questionId;
			viewResultsWindow.results = new ArrayList<>();
			viewResultsWindow.setTitle(name);
			viewResultsWindow.repaint();
			viewResultsWindow.findQuestionIndex();
			if (viewResultsWindow.questionIndex < viewResultsWindow.questions.size()) {
				viewResultsWindow.results = viewResultsWindow.questions.get(viewResultsWindow.questionIndex).results;
			}
		}
		viewResultsWindow.setVisible(true);

	}

	public void resultsUpdated(String questionId, List<Category> results) {
		if (inPollWindow != null) {
			inPollWindow.questions.stream()//
					.filter(q -> q.questionId.equals(questionId))//
					.forEach(q -> q.results = results);
		}
	}

}
