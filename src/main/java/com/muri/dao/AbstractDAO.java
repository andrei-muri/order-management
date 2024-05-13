package com.muri.dao;

import com.muri.connection.ConnectionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AbstractDAO<T> {
    protected final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        FileHandler fh;

        try {
            fh = new FileHandler("logs/events_dao.log");
            LOGGER.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());

            LOGGER.info("Logger initialized. Logger name: " + LOGGER.getName());
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, "Error occur in FileHandler in class " + AbstractDAO.class.getName());
        }
    }

    private String createSelectAllQuery(String table) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(table.toLowerCase());
        return sb.toString();
    }

    private String createInsertQuery(String table) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(table); sb.append(" (");
        for(Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            if(field.getName().equals("id")) {
                continue;
            }
            sb.append(field.getName()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());

        sb.append(") VALUES (");

        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            if(field.getName().equals("id")) {
                continue;
            }
            sb.append("?, ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(")");

        return sb.toString();
    }

//    private String createDeleteQuery(String table) {
//
//    }

    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery(type.getSimpleName());
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            List<T> objects = createObjects(resultSet);
            if(objects.isEmpty()) LOGGER.info("DAO::findAll The SELECT * query returned no element");
            assert objects != null;
            return objects;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public T insert(T object) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createInsertQuery(type.getSimpleName());
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            int parameterIndex = 1;
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName().equals("id")) {
                    continue;
                }
                Object value = field.get(object);
                statement.setObject(parameterIndex, value);
                parameterIndex++;
            }

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                LOGGER.info("Inserted " + rowsInserted + " row(s) into " + type.getSimpleName());
                resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    long generatedId = resultSet.getLong(1);
                    Field idField = object.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(object, generatedId);
                }
            } else {
                LOGGER.warning("Insert operation failed for " + type.getSimpleName());
                return null;
            }
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return object;
    }



    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for(Constructor constructor : ctors) {
            ctor = constructor;
            if(ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                assert ctor != null;
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException |
                 InvocationTargetException | SQLException | IntrospectionException e) {
            LOGGER.log(Level.SEVERE, "Error ini creating list of objects :: " + e.getMessage());
        }
        return list;
    }

}