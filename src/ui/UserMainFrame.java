package ui;

import session.SessionManager;

import javax.swing.*;
import java.awt.*;

public class UserMainFrame extends JFrame {

    public UserMainFrame() {
        setTitle("Ready 2 Read");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel(
                "Welcome, " + SessionManager.getUsername() + "! (User view coming soon)",
                SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        add(label);
    }
}
