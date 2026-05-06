package ui;

import dao.UserDAO;
import models.User;

import javax.swing.*;
import java.awt.*;

public class RegisterForm extends JDialog {

    private final JTextField usernameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmField = new JPasswordField(20);
    private final UserDAO userDAO = new UserDAO();
    private final JFrame parent;

    public RegisterForm(JFrame parent) {
        super(parent, "Register", true);
        this.parent = parent;
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Username:", "Email:", "Password:", "Confirm Password:"};
        JComponent[] fields = {usernameField, emailField, passwordField, confirmField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }

        JButton registerBtn = new JButton("Register");
        JButton cancelBtn = new JButton("Cancel");

        JPanel btnPanel = new JPanel();
        btnPanel.add(registerBtn);
        btnPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        registerBtn.addActionListener(e -> attemptRegister());
        cancelBtn.addActionListener(e -> dispose());

        add(panel);
    }

    private void attemptRegister() {
        String username = usernameField.getText().trim();
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm  = new String(confirmField.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (userDAO.usernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Username is already taken.",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userDAO.emailExists(email)) {
            JOptionPane.showMessageDialog(this, "Email is already registered.",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User newUser = new User(0, username, email, password, "user", null, null, null);
        userDAO.registerUser(newUser);
        JOptionPane.showMessageDialog(this, "Account created! Please sign in.",
                "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new LoginForm(parent).setVisible(true);
    }
}
