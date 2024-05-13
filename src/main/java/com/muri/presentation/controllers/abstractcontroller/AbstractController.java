package com.muri.presentation.controllers.abstractcontroller;

import com.muri.model.Client;
import com.muri.model.Order;
import com.muri.model.Product;
import com.muri.presentation.controllers.MenuController;
import com.muri.presentation.views.abstractview.AbstractAddView;
import com.muri.presentation.views.abstractview.AbstractEditView;
import com.muri.presentation.views.abstractview.AbstractView;
import com.muri.presentation.views.client.ClientEditView;
import com.muri.presentation.views.order.OrderEditView;
import com.muri.presentation.views.product.ProductEditView;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.*;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.Vector;

public class AbstractController<T> {
    protected AbstractView<T> view;
    protected AbstractAddView<T> addView;
    protected AbstractEditView<T> editView;
    protected Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractController(AbstractView<T> view, AbstractAddView<T> addView) {
        this.view = view;
        this.addView = addView;
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        view.setVisible(true);
        initTable();
        view.revalidate();
        view.repaint();
        setBackButton();
        setActions();
    }

    public void initTable() {
        Vector<String> columnNames = new Vector<>();
        for(Field field : type.getDeclaredFields()) {
            columnNames.add(field.getName());
        }
        Vector<Vector<String>> data = new Vector<>();

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        view.setTable(new JTable(model));
        JScrollPane scrollPane = new JScrollPane(view.getTable());
        view.add(scrollPane, BorderLayout.CENTER);
    }

    public void populateTableWithData(List<T> dataList) {
        DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
        model.setRowCount(0); // Clear existing data

        for (T dataItem : dataList) {
            Vector<Object> row = new Vector<>();
            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true); // Make private fields accessible
                try {
                    row.add(field.get(dataItem));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            model.addRow(row);
        }
    }

    private void setBackButton() {
        view.getBackButton().addActionListener((e) ->
        {
            view.setVisible(false);
            new MenuController();
        });
    }

    private void setActions() {
        view.getAddButton().addActionListener((e) -> {
            addView.setVisible(true);
            view.setVisible(false);
        });
        addView.getCancelButton().addActionListener((e) -> {
            addView.setVisible(false);
            view.setVisible(true);
        });
        view.getEditButton().addActionListener(e -> {
            int selectedRow = view.getTable().getSelectedRow();
            System.out.println(selectedRow);
            DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
            if (selectedRow >= 0) {
                T instance = getInstanceFromRow(selectedRow);

                Field[] fields = type.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    field.setAccessible(true); // Make field accessible
                    Object value = parseValue(field.getType(), model.getValueAt(selectedRow, i).toString());
                    try {
                        field.set(instance, value);
                    } catch(IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                if(type.getSimpleName().equals("Client")) {
                    editView = (AbstractEditView<T>) new ClientEditView((Client) instance);
                } else if(type.getSimpleName().equals("Product")) {
                    editView = (AbstractEditView<T>) new ProductEditView((Product) instance);
                } else {
                    editView = (AbstractEditView<T>) new OrderEditView((Order) instance);
                }
                editView.getCancelButton().addActionListener((e1) -> {
                    editView.setVisible(false);
                    view.setVisible(true);
                });
                editView.setVisible(true);
                view.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(view, "Please select a row to edit.");
            }
        });

    }

    protected T getInstanceFromTextFields() {
        addView.setData();
        T instance;
        try {
            instance = type.getDeclaredConstructor().newInstance();
            Map<String, Object> map = addView.getFieldMap();
            Field[] fields = type.getDeclaredFields();
            //int i = 1;
            for(int i = 1; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                Object value = parseValue(fieldType, map.get(field.getName()).toString());
                field.set(instance, value);
            }
//            for(Map.Entry<String, Object> entry : addView.getFieldMap().entrySet())
//            {
//                if(fields[i].getName().equals("id")) {i++; continue;}
//                Field field = fields[i];
//                field.setAccessible(true);
//                Class<?> fieldType = field.getType();
//                Object value = parseValue(fieldType, entry.getValue().toString());
//                field.set(instance, value);
//                i++;
//            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating instance from text fields", e);
        }
        return instance;
    }

    private T getInstanceFromRow(int row) {
        DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
        T instance;
        try {
            instance = type.getDeclaredConstructor().newInstance();
            Field[] fields = type.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                String cellValue = model.getValueAt(row, i).toString();
                Object value = parseValue(fieldType, cellValue);
                field.set(instance, value);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating instance from row data", e);
        }
        return instance;
    }

    protected Object parseValue(Class<?> type, String value) {
        try {
            if (Integer.class == type || int.class == type) {
                return Integer.parseInt(value);
            } else if (Double.class == type || double.class == type) {
                return Double.parseDouble(value);
            } else if (Float.class == type || float.class == type) {
                return Float.parseFloat(value);
            } else if (Long.class == type || long.class == type) {
                return Long.parseLong(value);
            } else if (Boolean.class == type || boolean.class == type) {
                return Boolean.parseBoolean(value);
            } else if (Byte.class == type || byte.class == type) {
                return Byte.parseByte(value);
            } else if (Short.class == type || short.class == type) {
                return Short.parseShort(value);
            }
            // Default case, no conversion needed
            return value;
        } catch (NumberFormatException e) {
            // Log the error or notify the user
            System.err.println("Error parsing value '" + value + "' for type " + type.getSimpleName() + ": " + e.getMessage());
            return null; // Return null or throw a custom exception if necessary
        }
    }
}
