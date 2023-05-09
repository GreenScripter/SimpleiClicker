package greenscripter.iclicker.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ScreenshotPreviewWindow extends JFrame {

	BufferedImage screenshot;
	long duration = -1;
	Thread autocloseThread;

	public ScreenshotPreviewWindow(BufferedImage screenshot) {
		this.screenshot = screenshot;
		this.add(new JPanel() {

			public void paintComponent(Graphics g) {
				double scaleX = getWidth() / (double) screenshot.getWidth();
				double scaleY = getHeight() / (double) screenshot.getHeight();

				double scale = Math.min(scaleX, scaleY);

				int width = (int) (screenshot.getWidth() * scale);
				int height = (int) (screenshot.getHeight() * scale);

				g.drawImage(screenshot, getWidth() / 2 - width / 2, getHeight() / 2 - height / 2, width, height, null);
			}

			public Dimension getPreferredSize() {
				return new Dimension(screenshot.getWidth(), screenshot.getHeight());
			}
		});
		this.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {}

			public void mousePressed(MouseEvent e) {
				if (duration != -1) {
					ScreenshotPreviewWindow alt = new ScreenshotPreviewWindow(screenshot);
					alt.setLocation(getLocation());
					alt.setSize(getSize());
					alt.setVisible(true);
					setVisible(false);
					duration = -1;
				}
			}

			public void mouseExited(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) {}

			public void mouseClicked(MouseEvent e) {}
		});
	}

	public ScreenshotPreviewWindow(BufferedImage screenshot, long ms) {
		this(screenshot);
		duration = ms;
		this.setUndecorated(true);
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (duration != -1) {
			autocloseThread = new Thread(() -> {
				try {
					Thread.sleep(duration);
					this.setVisible(false);
				} catch (Exception e) {
				}
			});
			autocloseThread.start();
		}
	}

}
