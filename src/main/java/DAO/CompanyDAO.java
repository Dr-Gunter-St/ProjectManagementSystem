package DAO;

import Model.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public final class CompanyDAO {

    private static Logger logger = LoggerFactory.getLogger(CompanyDAO.class);

    public static final Company getCompany(Connection connection, int id) {
        Company company = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM companies WHERE id = ?")){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            company = new Company();
            if (!resultSet.next()) return company;
            company.setId(resultSet.getInt("id"));
            company.setName(resultSet.getString("name"));

        } catch (SQLException e) {
            logger.error("Unable to prepare a statement");
            e.printStackTrace();
        }
        return company;
    }

    public static final Company getCompany(Connection connection, String name) {
        Company company = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM companies WHERE name = ?")){
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            company = new Company();
            if (!resultSet.next()) return company;
            company.setId(resultSet.getInt("id"));
            company.setName(resultSet.getString("name"));

        } catch (SQLException e) {
            logger.error("Unable to prepare a statement");
            e.printStackTrace();
        }
        return company;
    }

    public static final boolean changeName(Connection connection, int id, String newName){
        try (PreparedStatement statement = connection.prepareStatement("UPDATE companies SET name = ? WHERE id = ?")){
            statement.setString(1, newName);
            statement.setInt(2, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.info("No company with such id");
            return false;
        }
        return true;
    }

    public static final boolean deleteCompany(Connection connection, int id){
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM companies WHERE id = ?")){
            statement.setInt(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.info("No company with such id");
            return false;
        }
        return true;
    }

    public static final boolean addCompany(Connection connection, String name){
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO companies (name) VALUES (?)")){
            statement.setString(1, name);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.info("No company with such id");
            return false;
        }
        return true;
    }

}
