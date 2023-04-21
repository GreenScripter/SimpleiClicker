package greenscripter.iclicker.gui;

import java.util.ArrayList;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ClassListWindow extends JFrame {

	JLabel name;
	JPanel classesPanel;

	SimpleiClicker controller;
	List<ClassEntry> classes = new ArrayList<>();

	public ClassListWindow(SimpleiClicker controller) {
		this.controller = controller;

		name = new JLabel();
		classesPanel = new JPanel();
		classesPanel.setLayout(new BoxLayout(classesPanel, BoxLayout.Y_AXIS));
		JScrollPane scroll = new JScrollPane(classesPanel);

		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Courses");

		this.add(name, BorderLayout.NORTH);
		this.add(scroll);

		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
	}

	public void addClass(String uuid, String name) {
		ClassEntry entry = new ClassEntry(uuid, name);
		classesPanel.add(entry);
		classes.add(entry);
		this.revalidate();
	}

	public void clearClasses() {
		classesPanel.removeAll();
		classes.clear();
		this.revalidate();
	}

	public void applyActiveClasses(List<String> uuids) {
		for (ClassEntry e : classes) {
			e.setInProgress(uuids.contains(e.uuid));
		}
	}

	public class ClassEntry extends JPanel {

		String uuid;
		JLabel name;
		JButton start;

		public ClassEntry(String uuid, String name) {
			this.name = new JLabel(name);
			this.uuid = uuid;
			this.setLayout(new BorderLayout());
			this.add(this.name, BorderLayout.WEST);

			start = new JButton("Start Class");
			start.addActionListener(e -> controller.startClass(uuid));

			this.add(start, BorderLayout.EAST);
			this.setMaximumSize(new Dimension(this.getMaximumSize().width, this.getPreferredSize().height));
		}

		public void setInProgress(boolean inProgress) {
			if (inProgress) {
				start.setText("Resume Class");
			} else {
				start.setText("Start Class");
			}
		}
	}

}
