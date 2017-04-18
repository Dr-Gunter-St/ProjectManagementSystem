package DAO;

import Model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectDAO {

    private static Logger logger = LoggerFactory.getLogger(CompanyDAO.class);

    public static final Project getProject(Connection connection, int id) {
        Project project = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM projects WHERE id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            project = new Project();
            if (!resultSet.next()) return project;
            project.setId(resultSet.getInt("id"));
            project.setName(resultSet.getString("name"));

        } catch (SQLException e) {
            logger.error("Unable to prepare a statement");
            e.printStackTrace();
        }
        return project;
    }

    public static final Project getProject(Connection connection, String name) {
        Project project = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM projects WHERE name = ?")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            project = new Project();
            if (!resultSet.next()) return project;
            project.setId(resultSet.getInt("id"));
            project.setName(resultSet.getString("name"));

        } catch (SQLException e) {
            logger.error("Unable to prepare a statement");
            e.printStackTrace();
        }
        return project;
    }

    public static final boolean changeName(Connection connection, int id, String newName){
        try (PreparedStatement statement = connection.prepareStatement("UPDATE projects SET name = ? WHERE id = ?")) {
            statement.setString(1, newName);
            statement.setInt(2, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.info("No project with such id");
            return false;
        }
        return true;
    }

    public static final boolean deleteProject(Connection connection, int id){
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM projects WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.info("No project with such id");
            return false;
        }
        return true;
    }

    public static final boolean addProject(Connection connection, int company_id, int customer_id, String name){
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO projects (company_id, customer_id, name, cost) VALUES (?, ?, ?, 0)")){
            statement.setInt(1, company_id);
            statement.setInt(2, customer_id);
            statement.setString(3, name);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.info("No project with such id");
            return false;
        }
        return true;
    }

    /*
    Is used only from DeveloperDAO after crating or deleting
     a developer and after developer salary is changed
    */

    public static final boolean updateCost(Connection connection, int id, int term) throws SQLException {
        try{
            int currentCost = 0;
            PreparedStatement costStat = connection.prepareStatement("SELECT cost FROM projects WHERE id=?");
            costStat.setInt(1, id);
            ResultSet costRes = costStat.executeQuery();
            if (!costRes.next()) throw new SQLException("No cost in project " + id);
            currentCost = costRes.getInt("cost");

            PreparedStatement statement = connection.prepareStatement("UPDATE projects SET cost = ? WHERE id = ?");
            statement.setInt(1, currentCost + term);
            statement.setInt(2, id);

            statement.executeUpdate();

            connection.commit();

        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.error("Unable to update projects cost");
            throw new SQLException("Unable to update projects cost");
        }

        return true;
    }

}
