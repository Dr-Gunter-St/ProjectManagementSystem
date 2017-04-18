package DAO;

import Model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public final class CustomerDAO {

    private static Logger logger = LoggerFactory.getLogger(CompanyDAO.class);

    public static final Customer getCustomer(Connection connection, int id) {
        Customer customer = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM customers WHERE id = ?")){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            customer = new Customer();
            if (!resultSet.next()) return customer;
            customer.setId(resultSet.getInt("id"));
            customer.setName(resultSet.getString("name"));

        } catch (SQLException e) {
            logger.error("Unable to prepare a statement");
            e.printStackTrace();
        }
        return customer;
    }

    public static final Customer getCustomer(Connection connection, String name) {
        Customer customer = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM customers WHERE name = ?")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            customer = new Customer();
            if (!resultSet.next()) return customer;
            customer.setId(resultSet.getInt("id"));
            customer.setName(resultSet.getString("name"));

        } catch (SQLException e) {
            logger.error("Unable to prepare a statement");
            e.printStackTrace();
        }
        return customer;
    }

    public static final boolean changeName(Connection connection, int id, String newName){
        try (PreparedStatement statement = connection.prepareStatement("UPDATE customers SET name = ? WHERE id = ?")) {
            statement.setString(1, newName);
            statement.setInt(2, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.info("No customer with such id");
            return false;
        }
        return true;
    }

    public static final boolean deleteCustomer(Connection connection, int id){
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM customers WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.info("No customer with such id");
            return false;
        }
        return true;
    }

    public static final boolean addCustomer(Connection connection, String name){
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO customers (name) VALUES (?)")) {
            statement.setString(1, name);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.info("No customer with such id");
            return false;
        }
        return true;
    }

}
