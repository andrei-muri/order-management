package com.muri.presentation.views.abstractview;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class AbstractAddView<T> extends JFrame {
    JButton addButton;
    JButton cancelButton;
    Class<T> type;

    public AbstractAddView() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        // Set the frame properties
        setTitle(type.getSimpleName());
        initializeForm();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2));
        setLocationRelativeTo(null);
        pack();
        setVisible(false);
    }

    private void initializeForm() {
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if(!field.getName().equals("id")) {
                JLabel label = new JLabel(field.getName());
                JTextField textField = new JTextField(20);
                add(label);
                add(textField);
            }
            //fieldMap.put(field.getName(), textField);
        }

        addButton = new JButton("Add");
        addButton.addActionListener(e -> {});
        add(addButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        add(cancelButton);
        revalidate();
        repaint();
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }
}
