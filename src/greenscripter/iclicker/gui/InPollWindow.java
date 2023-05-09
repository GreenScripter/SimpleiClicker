package greenscripter.iclicker.gui;

import java.util.ArrayList;
import java.util.List;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import greenscripter.iclicker.gui.ViewResultsWindow.Category;

public class InPollWindow extends JFrame {

	SimpleiClicker controller;
	String activityId;
	List<PollQuestion> questions = new ArrayList<>();
	boolean anyQuestionStarted = false;

	public InPollWindow(SimpleiClicker controller, String activityId, String name) throws AWTException {
		this.controller = controller;
		this.activityId = activityId;
		this.setTitle(name);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JButton startQuestion = new JButton("Start Question");
		JButton viewResults = new JButton("View Results");
		viewResults.setEnabled(false);

		JButton endPoll = new JButton("End Poll");

		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

		this.add(startQuestion);
		this.add(viewResults);
		this.add(endPoll);
		this.pack();
		this.setLocationRelativeTo(null);

		Robot r = new Robot();

		startQuestion.addActionListener(e -> {
			this.setVisible(false);
			SwingUtilities.invokeLater(() -> {
				BufferedImage image = r.createScreenCapture(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds());
				this.setVisible(true);
				controller.startQuestion(this.activityId, image);
				viewResults.setEnabled(true);
				anyQuestionStarted = true;
			});

		});
		viewResults.addActionListener(e -> {
			controller.viewResults(this.questions.get(0).questionId, name + " Results");
		});

		endPoll.addActionListener(e -> controller.endPoll(this.activityId));

	}

	public static class PollQuestion {

		String questionId;
		String name;
		List<Category> results = new ArrayList<>();

		public PollQuestion(String questionId2, String name) {
			questionId = questionId2;
			this.name = name;
		}

	}
}
