package greenscripter.iclicker.gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class InQuestionWindow extends JFrame {

	SimpleiClicker controller;
	String questionId;

	JLabel time;
	long start = System.currentTimeMillis();
	Thread timeThread;

	public InQuestionWindow(SimpleiClicker controller, String questionId, String name) {
		this.controller = controller;
		this.questionId = questionId;

		time = new JLabel("00:00");
		time.setFont(time.getFont().deriveFont(25f));

		this.setTitle(name);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JButton endQuestion = new JButton("End Question");
		JButton viewResults = new JButton("View Results");

		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

		this.add(time);
		this.add(endQuestion);
		this.add(viewResults);
		this.pack();
		this.setLocationRelativeTo(null);

		endQuestion.addActionListener(e -> controller.endQuestion(this.questionId));
		viewResults.addActionListener(e -> controller.viewResults(this.questionId, name + " Results"));

	}

	public void setVisible(boolean v) {
		super.setVisible(v);
		if (v) {
			timeThread = new Thread(() -> {
				try {
					while (!Thread.interrupted()) {
						long ms = System.currentTimeMillis() - start;
						long seconds = ms / 1000;
						long minutes = seconds / 60;
						seconds = seconds % 60;
						time.setText(String.format("%02d:%02d", minutes, seconds));
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
				}
			});
			timeThread.start();
		} else {
			timeThread.interrupt();
		}
	}
}
