package DAO;

import Model.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SkillsDAO {

    private static final Logger logger = LoggerFactory.getLogger(SkillsDAO.class);

    public static final List<Skill> getAllSkills(Connection connection){
        List<Skill> skills = null;
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM skills")){
            ResultSet resultSet = statement.executeQuery();

            skills = new LinkedList<>();
            while (resultSet.next()){
                Skill skill = new Skill();
                skill.setId(resultSet.getInt("id"));
                skill.setSkillType(resultSet.getString("type"));
                skills.add(skill);
            }

        } catch (SQLException e){
            logger.error("Unable to get Skills table");
        }
        return skills;
    }

    public static final boolean deleteDeveloperSkills(Connection connection, int dev_id){
        try(PreparedStatement statement = connection.prepareStatement("DELETE * FROM developer_skills WHERE dev_id = ?")){
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.error("Error while deleting developer`s skills");
            return false;
        }
        return true;
    }

    /*
    Is used only from DeveloperDAO
    */

    public static boolean addDeveloperSkills(Connection connection, int dev_id, List<Skill> dev_skills) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO developer_skills VALUES (?, ?)")){
            statement.setInt(1, dev_id);
            for (int i = 0; i < dev_skills.size(); i ++){
                Skill s = dev_skills.get(i);
                statement.setInt(2, s.getId());
                statement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {}
            logger.error("Error while inserting skills");
            throw new SQLException("Error while inserting skills");
        }

        return true;
    }

}
