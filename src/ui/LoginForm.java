package ui;

import dao.UserDAO;
import models.User;
import session.SessionManager;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JDialog {

    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final UserDAO userDAO = new UserDAO();

    public LoginForm(JFrame parent) {
        super(parent, "Sign In", true);
        setSize(350, 220);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JButton loginBtn = new JButton("Login");
        JButton cancelBtn = new JButton("Cancel");

        JPanel btnPanel = new JPanel();
        btnPanel.add(loginBtn);
        btnPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        loginBtn.addActionListener(e -> attemptLogin());
        cancelBtn.addActionListener(e -> dispose());

        add(panel);
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userDAO.login(username, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid username or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        } else {
            SessionManager.login(user.getUserID(), user.getUsername(), user.getRole());
            dispose();
        }
    }
}
