package ui;

import javax.swing.*;
import java.awt.*;

public class AdminMainFrame extends JFrame {

    public AdminMainFrame() {
        setTitle("Ready 2 Read");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Welcome, Admin! (Admin view coming soon)", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        add(label);
    }
}
