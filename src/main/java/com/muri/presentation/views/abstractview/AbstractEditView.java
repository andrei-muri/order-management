package com.muri.presentation.views.abstractview;

import com.muri.presentation.controllers.abstractcontroller.AbstractController;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractEditView<T> extends JFrame implements ObjectManipulator{
    JButton editButton = new JButton("Add");
    JButton cancelButton  = new JButton("Cancel");
    Class<T> type;
    List<JTextField> textFieldList = new ArrayList<>();
    Map<String, Object> fieldMap = new HashMap<>();
    T instance;

    public AbstractEditView() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        // Set the frame properties
        setTitle(type.getSimpleName());
        setVisible(false);
    }

    public void initForm(T instance) {
        initializeForm(instance);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2));
        setLocationRelativeTo(null);
        pack();
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
                    textFieldList.add(textField);
                    add(label);
                    add(textField);
                }
                //fieldMap.put(field.getName(), textField);
            }
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }

        //addButton.addActionListener(e -> {});
        add(editButton);

        //cancelButton.addActionListener(e -> setVisible(false));
        add(cancelButton);
        revalidate();
        repaint();
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getEditButton() {
        return editButton;
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

    @Override
    public Map<String, Object> getFieldMap() {
        return fieldMap;
    }

    public void setDefaultText(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (int i = 1; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true); // Make field accessible
            try {
                Object value = AbstractController.parseValue(field.getType(), field.get(t).toString());
                assert value != null;
                textFieldList.get(i - 1).setText(value.toString());
            } catch(IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<JTextField> getTextFieldList() {
        return textFieldList;
    }
}
