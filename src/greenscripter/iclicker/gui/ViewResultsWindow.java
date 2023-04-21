package greenscripter.iclicker.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import greenscripter.iclicker.gui.InPollWindow.PollQuestion;
import greenscripter.iclicker.gui.ViewResultsWindow.Category.Correct;

public class ViewResultsWindow extends JFrame {

	SimpleiClicker controller;
	String classId;
	String activityId;
	String questionId;
	GraphPanel graph;
	List<Category> results = new ArrayList<>();
	List<PollQuestion> questions;
	JButton next;
	JButton previous;

	int questionIndex;

	ExecutorService ex = Executors.newFixedThreadPool(1);
	private Thread checkThread;

	public static void main(String[] args) {
		ViewResultsWindow window = new ViewResultsWindow("Test");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.results.add(new Category("A", 1, 10, Correct.INCORRECT));
		window.results.add(new Category("B", 2, 20, Correct.CORRECT));
		window.results.add(new Category("C", 3, 0, Correct.UNGRADED));
		window.results.add(new Category("D", 4, 40, Correct.INCORRECT));
		window.results.add(new Category("E", 5, 100, Correct.INCORRECT));

		window.setVisible(true);
	}

	public ViewResultsWindow(String name) {
		this.setTitle(name);

		this.setLayout(new BorderLayout());

		JPanel topSpace = new JPanel();
		topSpace.setLayout(new BoxLayout(topSpace, BoxLayout.X_AXIS));

		next = new JButton("Next");
		previous = new JButton("Previous");

		topSpace.add(previous);
		topSpace.add(next);

		graph = new GraphPanel();
		this.add(graph);
		this.add(topSpace, BorderLayout.NORTH);

		this.pack();
		this.setLocationRelativeTo(null);

		next.addActionListener(e -> {
			questionIndex++;
			changeQuestionIndex();
			if (results.isEmpty()) {
				updateResults();
			}
		});

		previous.addActionListener(e -> {
			questionIndex--;
			changeQuestionIndex();
			if (results.isEmpty()) {
				updateResults();
			}
		});
	}

	public ViewResultsWindow(SimpleiClicker controller, String classId, String activityId, String questionId, String name, List<PollQuestion> questions) {
		this(name);
		this.controller = controller;
		this.classId = classId;
		this.activityId = activityId;
		this.questionId = questionId;
		this.questions = questions;
		ex.submit(() -> {
			updateResults();
		});
		findQuestionIndex();

	}

	public void changeQuestionIndex() {
		PollQuestion q = questions.get(questionIndex);
		questionId = q.questionId;
		this.setTitle(q.name + " Results");
		this.results = q.results;
		setButtons();
		repaint();
	}

	public void findQuestionIndex() {
		for (int i = 0; i < questions.size(); i++) {
			if (questions.get(i).questionId.equals(questionId)) {
				questionIndex = i;
				setButtons();
				return;
			}
		}
		questionIndex = questions.size();
		setButtons();
	}

	public void setButtons() {
		if (questionIndex + 1 >= questions.size()) {
			next.setEnabled(false);
		} else {
			next.setEnabled(true);
		}
		if (questionIndex - 1 < 0) {
			previous.setEnabled(false);
		} else {
			previous.setEnabled(true);
		}
	}

	public void setVisible(boolean v) {
		super.setVisible(v);
		if (v) {
			checkThread = new Thread(() -> {
				try {
					while (!Thread.interrupted()) {
						if (results.isEmpty() || (controller.inQuestionWindow != null && questionId.equals(controller.inQuestionWindow.questionId))) {
							updateResults();
						}
						Thread.sleep(10000);
					}
				} catch (InterruptedException e) {
				}
			});
			checkThread.start();
		} else {
			checkThread.interrupt();
		}
	}

	public void updateResults() {
		String requestedId = questionId;
		var response = controller.getQuestionResults(classId, activityId, requestedId);
		if (response != null) {
			if (requestedId.equals(questionId)) synchronized (results) {

				results.clear();
				var question = response.questions.get(0);
				for (var answer : question.answerOverview) {
					results.add(new Category(answer.answer(), answer.count(), answer.percentageOfTotalResponses(), question.graded ? (answer.correct() ? Correct.CORRECT : Correct.INCORRECT) : Correct.UNGRADED));
				}
				for (String s : List.of("A", "B", "C", "D", "E")) {
					if (!results.stream().anyMatch(c -> c.name.equals(s))) {
						results.add(new Category(s, 0, 0, question.graded ? Correct.INCORRECT : Correct.UNGRADED));
					}
				}
				results.sort(Comparator.comparing(c -> c.name));

				this.repaint();
				controller.resultsUpdated(questionId, results);
			}
		}
	}

	public class GraphPanel extends JPanel implements MouseListener {

		public GraphPanel() {
			this.setPreferredSize(new Dimension(400, 200));
			this.addMouseListener(this);
		}

		public void paintComponent(Graphics g) {
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight());
			synchronized (results) {
				if (results.size() == 0) return;

				int pos = 0;
				int offset = getWidth() / results.size();

				FontMetrics fm = g.getFontMetrics();

				for (Category c : results) {
					switch (c.correct) {
						case CORRECT:
							g.setColor(new Color(60, 212, 60));
							break;
						case INCORRECT:
							g.setColor(new Color(222, 60, 60));
							break;
						case UNGRADED:
							g.setColor(new Color(60, 119, 222));
							break;
					}
					int height = (int) ((getHeight() - 10) * c.percent / 100) + 10;
					g.fillRect(pos + (int) (offset * 0.05), getHeight() - height, (int) (offset * 0.9), height);

					String percent = String.format("%.1f", c.percent) + "%";
					g.setColor(Color.black);

					g.drawString(percent, pos + offset / 2 - fm.stringWidth(percent) / 2, getHeight() - fm.getDescent());

					g.drawString(c.count + "", pos + offset / 2 - fm.stringWidth(c.count + "") / 2, getHeight() - fm.getDescent() - fm.getHeight());

					pos += offset;

				}
			}

		}

		public void mouseClicked(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			int pos = 0;
			int offset = getWidth() / results.size();
			synchronized (results) {
				for (Category c : results) {
					Rectangle r = new Rectangle(pos + (int) (offset * 0.05), 0, (int) (offset * 0.9), getHeight());
					if (r.contains(e.getX(), e.getY())) {
						c.correct = c.correct.toggle();

						results.stream()//
								.filter(ct -> ct.correct.equals(Correct.UNGRADED))//
								.forEach(ct -> ct.correct = Correct.INCORRECT);

						ex.submit(() -> {
							controller.setAnswer(activityId, questionId, c.name, c.correct.equals(Correct.CORRECT));
						});

						controller.resultsUpdated(questionId, results);

						this.repaint();
						return;
					}

					pos += offset;

				}
			}
		}

		public void mouseReleased(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}
	}

	public static class Category {

		String name;
		int count;
		double percent;
		Correct correct;

		public Category(String name, int count, double percent, Correct correct) {
			this.name = name;
			this.count = count;
			this.percent = percent;
			this.correct = correct;
		}

		public enum Correct {

			CORRECT, INCORRECT, UNGRADED;

			public Correct toggle() {
				return equals(CORRECT) ? INCORRECT : CORRECT;
			}
		}
	}
}
