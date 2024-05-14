package com.muri.presentation.views.abstractview;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractAddView<T> extends JFrame implements ObjectManipulator{
    JButton addButton;
    JButton cancelButton;
    Class<T> type;

    List<JTextField> textFieldList = new ArrayList<>();

    Map<String, Object> fieldMap = new HashMap<>();

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
                textFieldList.add(textField);
                add(label);
                add(textField);
                //fieldMap.put(field.getName(), textField.getText());
            }

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

    @Override
    public void setData() {
        int i = 0;
        for(Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            if(!field.getName().equals("id")) {
                fieldMap.put(field.getName(), textFieldList.get(i++).getText());
            }
        }
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public Map<String, Object> getFieldMap() {
        return fieldMap;
    }
}
