package greenscripter.iclicker.gui;

import java.util.ArrayList;
import java.util.List;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
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

	static private BufferedImage text2Img(String message) {
		BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
		g.setColor(Color.BLACK);
		g.drawString(message, 0, 200);
		return image;
	}

	static private BufferedImage captureScreen() {
		// If SCREENCAPCMD is defined, it should be a commandline that generates an image
		// on stdout. The following command will do that on ubuntu:
		//     SCREENCAPCMD="spectacle -b -n -o /proc/self/fd/1" java ...
		String screenCapCmd = System.getenv("SCREENCAPCMD");
		try {
			if (screenCapCmd == null) {
				Robot r = new Robot();
				return r.createScreenCapture(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds());
			} else {
				return ImageIO.read(Runtime.getRuntime().exec(screenCapCmd).getInputStream());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return text2Img(ex.getMessage());
		}
	}

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

		startQuestion.addActionListener(e -> {
			this.setVisible(false);
			SwingUtilities.invokeLater(() -> {
				BufferedImage image = captureScreen();
				this.setVisible(true);
				ScreenshotPreviewWindow preview = new ScreenshotPreviewWindow(image, 5000);
				preview.setSize(this.getWidth(), this.getWidth());
				preview.setLocation(getX(), getY() + getHeight());
				preview.setVisible(true);
				SwingUtilities.invokeLater(() -> {
					controller.startQuestion(this.activityId, image);
					viewResults.setEnabled(true);
					anyQuestionStarted = true;
				});
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
