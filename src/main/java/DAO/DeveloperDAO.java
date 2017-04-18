package DAO;

import Model.Developer;
import Model.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public class DeveloperDAO {

    private static Logger logger = LoggerFactory.getLogger(CompanyDAO.class);

    public static final Developer getDeveloper(Connection connection, int id) {
        Developer developer = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM developers WHERE id = ?")){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) return developer;
            developer = new Developer();
            developer.setId(resultSet.getInt("id"));
            developer.setName(resultSet.getString("name"));

        } catch (SQLException e) {
            logger.error("Unable to prepare a statement");
            e.printStackTrace();
        }
        return developer;
    }

    public static final Developer getDeveloper(Connection connection, String name) {
        Developer developer = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM developers WHERE name = ?")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();


            if (!resultSet.next()) return developer;
            developer = new Developer();
            developer.setId(resultSet.getInt("id"));
            developer.setName(resultSet.getString("name"));

        } catch (SQLException e) {
            logger.error("Unable to prepare a statement");
            e.printStackTrace();
        }
        return developer;
    }

    public static final Integer getSalary(Connection connection, int id) {
        Integer salary = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT salary FROM developers WHERE id = ?")){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) return salary;
            salary = resultSet.getInt("salary");

        } catch (SQLException e) {
            logger.error("Unable to prepare a statement");
            e.printStackTrace();
        }
        return salary;
    }

    private static final Integer getProjectID(Connection connection, int id){
        Integer project_id = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT project_id FROM developers WHERE id = ?")){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) return project_id;
            project_id = resultSet.getInt("project_id");

        } catch (SQLException e) {
            logger.error("Unable to prepare a statement");
            e.printStackTrace();
        }
        return project_id;
    }

    public static final boolean changeName(Connection connection, int id, String newName){
        try (PreparedStatement statement = connection.prepareStatement("UPDATE developers SET name = ? WHERE id = ?")) {
            statement.setString(1, newName);
            statement.setInt(2, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.info("No developer with such id");
            return false;
        }
        return true;
    }

    public static final boolean deleteDeveloper(Connection connection, int id){
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM developers WHERE id = ?")) {
            statement.setInt(1, id);
            if (statement.executeUpdate() == 0) return false;

            int salary = getSalary(connection, id);
            int project_id = getProjectID(connection, id);
            ProjectDAO.updateCost(connection, project_id, -salary);
            SkillsDAO.deleteDeveloperSkills(connection, id);
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.info("No developer with such id");
            return false;
        }

        return true;
    }

    public static final boolean addDeveloper(Connection connection, String name, int project_id, int company_id, int salary, List<Skill> devSkills){
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO developers (name, project_id, company_id, salary) VALUES (?, ?, ?, ?)")) {

            statement.setString(1, name);
            statement.setInt(2, project_id);
            statement.setInt(3, company_id);
            statement.setInt(4, salary);
            statement.executeUpdate();

            ProjectDAO.updateCost(connection, project_id, salary);

            SkillsDAO.addDeveloperSkills(connection, getLastDevId(connection), devSkills);

            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.error("Cannot add a developer");
            return false;
        }
        return true;
    }


    private static final int getLastDevId(Connection connection){
        int id = -1;
        try (PreparedStatement statement = connection.prepareStatement("SELECT id FROM developers")){
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                id = resultSet.getInt("id");
            }
            return id;
        } catch (SQLException e){
            logger.error("Error occured while getting last dev id");
        }

        return id;
    }
}
