package com.muri.dao;

import com.muri.connection.ConnectionFactory;
import com.muri.model.Bill;

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

/**
 * Data access abstract class that offers access to the database for CRUD operations on an order management table.
 * It is meant for inheriting by {@link com.muri.dao.BillDAO}, {@link com.muri.dao.ClientDAO},
 * {@link com.muri.dao.OrderDAO}, {@link com.muri.dao.ProductDAO}.
 * @param <T> the models: {@link com.muri.model.Bill}, {@link com.muri.model.Order}, {@link com.muri.model.Product}, {@link com.muri.model.Client}
 * @author Muresan Andrei UTCN Computer Science 30425_2 2024
 */
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

    private String createInsertQuery(String table) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        if(table.equals("Order")) sb.append("order_management.");
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
    private String createUpdateQuery(String table) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(table);
        sb.append(" SET ");

        Field[] fields = type.getDeclaredFields();
        boolean first = true;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals("id")) {
                continue;
            }
            if (!first) {
                sb.append(", ");
            }
            sb.append(field.getName()).append(" = ?");
            first = false;
        }

        sb.append(" WHERE id = ?");

        return sb.toString();
    }
    private String createDeleteQuery(String table) {
        return "DELETE FROM " + (type.getSimpleName().equalsIgnoreCase("Order") ? "order_management." : "") + table + " WHERE id = ?";
    }
    private String createSelectAllQuery(String table) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        if(table.equals("Order")) sb.append("order_management.");
        sb.append(table.toLowerCase());
        return sb.toString();
    }
    private String createFindByIdQuery(String table) {
        return "SELECT * FROM " + table + " WHERE id = ?";
    }


    /**
     * Inserts an object into the database
     * @param object to be inserted
     * @return the inserted object
     */
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

    /**
     * Updates an object from the database
     * @param object to be updated
     * @return the updated object
     */
    public T update(T object) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createUpdateQuery(type.getSimpleName());
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

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

            Field idField = object.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(object);
            statement.setObject(parameterIndex, idValue);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                LOGGER.info("Updated " + rowsUpdated + " row(s) in " + type.getSimpleName());
            } else {
                LOGGER.warning("Update operation failed for " + type.getSimpleName());
                return null;
            }
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.log(Level.WARNING, type.getName() + " DAO:update: " + e.getMessage(), e);
            return null;
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return object;
    }

    /**
     * Deletes an object from the database
     * @param object to be deleted
     * @return the deleted object
     */
    public int delete(T object) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createDeleteQuery(type.getSimpleName());
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            Field idField = object.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(object);
            statement.setObject(1, idValue);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                LOGGER.info("Deleted " + rowsUpdated + " row(s) in " + type.getSimpleName());
                return rowsUpdated;
            } else {
                LOGGER.warning("Delete operation failed for " + type.getSimpleName());
                return 0;
            }
        } catch(SQLException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.log(Level.SEVERE, "Exception in delete :: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return 0;
    }

    /**
     * Retrieves all the objects
     * @return {@code List} of models
     */
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

    /**
     * Returns an object that is found after its id
     * @param id model's id
     * @return the model
     */
    public T findById(int id) {
        T instance = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createFindByIdQuery(type.getSimpleName());
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setObject(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).getFirst();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "DAO::findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
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
            LOGGER.log(Level.SEVERE, "Error in creating list of objects :: " + e.toString());
        }
        return list;
    }



}
