package greenscripter.iclicker.gui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.google.gson.Gson;

import greenscripter.iclicker.api.IClickerInstance;
import greenscripter.iclicker.api.data.request.LoginRequest;

public class LoginWindow extends JFrame {

	JTextField email;
	JPasswordField password;
	JCheckBox remember;
	Object lock = new Object();

	public LoginWindow() {
		email = new JTextField(25);
		password = new JPasswordField(25);
		remember = new JCheckBox("Remember me");
		remember.setSelected(true);

		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Simple iClicker Login");

		this.add(new JLabel("Email"));
		this.add(email);
		this.add(new JLabel("Password"));
		this.add(password);
		this.add(remember);

		JButton login = new JButton("Login");
		login.addActionListener(this::loginButton);

		this.add(login);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	private void loginButton(ActionEvent e) {
		this.setVisible(false);
		synchronized (lock) {
			lock.notify();
		}
	}

	public LoginRequest showLogin() throws InterruptedException {
		this.setVisible(true);
		synchronized (lock) {
			lock.wait();
		}
		return new LoginRequest(email.getText(), new String(password.getPassword()));
	}

	public static void login(IClickerInstance instance) throws InterruptedException {
		LoginWindow login = new LoginWindow();

		LoginRequest r = login.showLogin();

		instance.email = r.email();
		instance.password = r.password();
		instance.remember = login.remember.isSelected();

		try {
			instance.login();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getClass() + " " + e.getMessage(), "Login Error", JOptionPane.WARNING_MESSAGE);
			login(instance);
			return;
		}

		if (instance.remember) {
			try {
				File auth = new File("iclickerauth.json");
				OutputStream out = new FileOutputStream(auth);
				out.write(new Gson().toJson(instance).getBytes());
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
