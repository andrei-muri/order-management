package com.muri.presentation.views.abstractview;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.ParameterizedType;

public class AbstractView<T> extends JFrame{
    private JTable table;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;

    Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractView() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        // Set the frame properties
        setTitle(type.getSimpleName());
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());  // Use BorderLayout for the main frame

        // Create the button panel and buttons
        initializeButtons();

        // Make the frame visible
        setVisible(false);
    }

    private void initializeButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // Use FlowLayout for equal spacing and center alignment

        // Initialize buttons
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        backButton = new JButton("Back");

        // Add buttons to the panel
        buttonPanel.add(backButton);
        if(!type.getSimpleName().equals("Bill")) buttonPanel.add(addButton);
        if(!type.getSimpleName().equals("Order") && !type.getSimpleName().equals("Bill")) buttonPanel.add(editButton);
        if(!type.getSimpleName().equals("Order")  && !type.getSimpleName().equals("Bill")) buttonPanel.add(deleteButton);


        add(buttonPanel, BorderLayout.SOUTH);  // Add the button panel to the south region of the frame
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getBackButton() {
        return backButton;
    }
}
