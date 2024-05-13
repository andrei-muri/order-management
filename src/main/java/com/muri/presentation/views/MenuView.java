package com.muri.presentation.views;

import javax.swing.*;
import java.awt.*;

public class MenuView extends JFrame {
    private final JButton clientButton;
    private final JButton productButton;
    private final JButton orderButton;
    private final JButton logButton;

    public MenuView() {
        // Initialize buttons
        clientButton = new JButton("Clients");
        productButton = new JButton("Products");
        orderButton = new JButton("Orders");
        logButton = new JButton("Log");

        // Create a panel with BoxLayout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Adds padding around the panel

        // Calculate the maximum size needed for all buttons
        Dimension maxButtonSize = getMaxButtonSize(clientButton, productButton, orderButton, logButton);

        // Set each button's preferred and maximum size
        clientButton.setPreferredSize(maxButtonSize);
        clientButton.setMaximumSize(maxButtonSize);
        productButton.setPreferredSize(maxButtonSize);
        productButton.setMaximumSize(maxButtonSize);
        orderButton.setPreferredSize(maxButtonSize);
        orderButton.setMaximumSize(maxButtonSize);
        logButton.setPreferredSize(maxButtonSize);
        logButton.setMaximumSize(maxButtonSize);

        // Add buttons to the panel with space between them
        addComponentWithSpace(buttonPanel, clientButton);
        addComponentWithSpace(buttonPanel, productButton);
        addComponentWithSpace(buttonPanel, orderButton);
        addComponentWithSpace(buttonPanel, logButton);

        // Center the button panel in the frame
        add(buttonPanel, BorderLayout.CENTER);

        // Set the frame properties
        setTitle("Menu View");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(false);
    }

    private Dimension getMaxButtonSize(JButton... buttons) {
        int maxWidth = 0;
        int maxHeight = 0;
        for (JButton button : buttons) {
            Dimension dim = button.getPreferredSize();
            maxWidth = Math.max(maxWidth, dim.width);
            maxHeight = Math.max(maxHeight, dim.height);
        }
        return new Dimension(maxWidth, maxHeight);
    }

    private void addComponentWithSpace(JPanel panel, JButton btn) {
        panel.add(Box.createVerticalStrut(10));
        panel.add(btn);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public JButton getClientButton() {
        return clientButton;
    }

    public JButton getProductButton() {
        return productButton;
    }

    public JButton getOrderButton() {
        return orderButton;
    }

    public JButton getLogButton() {
        return logButton;
    }
}
