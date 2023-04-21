package greenscripter.iclicker.gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

public class InClassWindow extends JFrame {

	SimpleiClicker controller;
	String classId;
	String meetingId;

	public InClassWindow(SimpleiClicker controller, String classId, String meetingId, String name) {
		this.controller = controller;
		this.classId = classId;
		this.meetingId = meetingId;
		this.setTitle(name);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JButton endClass = new JButton("End Class");
		JButton startPoll = new JButton("Start Poll");

		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

		this.add(startPoll);
		this.add(endClass);
		this.pack();
		this.setLocationRelativeTo(null);

		endClass.addActionListener(e -> controller.endClass(this.classId));
		startPoll.addActionListener(e -> controller.startPoll(this.classId, this.meetingId));

	}
}
