package com.muri.presentation.views.abstractview;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class AbstractEditView<T> extends JFrame {
    JButton addButton = new JButton("Add");
    JButton cancelButton  = new JButton("Cancel");
    Class<T> type;

    public AbstractEditView(T instance) {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        // Set the frame properties
        setTitle(type.getSimpleName());
        initializeForm(instance);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2));
        setLocationRelativeTo(null);
        pack();
        setVisible(false);
    }

    private void initializeForm(T instance) {
        try {
            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                if(!field.getName().equals("id")) {
                    field.setAccessible(true);
                    JLabel label = new JLabel(field.getName());
                    JTextField textField = new JTextField(20);
                    if (instance != null) {
                        Object value = field.get(instance);
                        textField.setText(value.toString());
                    }
                    add(label);
                    add(textField);
                }
                //fieldMap.put(field.getName(), textField);
            }
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }

        addButton.addActionListener(e -> {});
        add(addButton);

        cancelButton.addActionListener(e -> setVisible(false));
        add(cancelButton);
        revalidate();
        repaint();
    }

    public JButton getCancelButton() {
        return cancelButton;
    }
}
