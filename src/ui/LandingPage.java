package ui;

import session.SessionManager;

import javax.swing.*;
import java.awt.*;

public class LandingPage extends JFrame {

    public LandingPage() {
        setTitle("Ready 2 Read");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel title = new JLabel("Ready 2 Read");
        title.setFont(new Font("Serif", Font.BOLD, 32));
        gbc.gridy = 0;
        panel.add(title, gbc);

        JLabel subtitle = new JLabel("Track your reading. Share your reviews.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridy = 1;
        panel.add(subtitle, gbc);

        JButton signInBtn = new JButton("Sign In");
        signInBtn.setPreferredSize(new Dimension(140, 35));
        gbc.gridy = 2;
        panel.add(signInBtn, gbc);

        JButton registerBtn = new JButton("Register");
        registerBtn.setPreferredSize(new Dimension(140, 35));
        gbc.gridy = 3;
        panel.add(registerBtn, gbc);

        signInBtn.addActionListener(e -> {
            LoginForm loginForm = new LoginForm(this);
            loginForm.setVisible(true);
            if (SessionManager.isLoggedIn()) {
                onLoginSuccess();
            }
        });

        registerBtn.addActionListener(e -> {
            RegisterForm registerForm = new RegisterForm(this);
            registerForm.setVisible(true);
        });

        add(panel);
    }

    private void onLoginSuccess() {
        dispose();
        if (SessionManager.isAdmin()) {
            new AdminMainFrame().setVisible(true);
        } else {
            new UserMainFrame().setVisible(true);
        }
    }
}
