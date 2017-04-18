import DAO.*;
import Model.Developer;
import Model.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.*;
import java.io.IOException;
import java.sql.*;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static DataSource dataSource;

    public static void main(String[] args){
        try {
            dataSource = DataSource.getInstance();

            try (Connection connection = dataSource.getConnection()){
                ProjectDAO.addProject(connection, 1, 5, "Nuggets"); // id = 9

                Skill skill = new Skill();
                skill.setSkillType("Java");
                skill.setId(1);
                Skill skill1 = new Skill();
                skill1.setId(2);
                skill.setSkillType("C++");
                Skill skill2 = new Skill();
                skill2.setId(8);
                skill.setSkillType("Golang");


                LinkedList<Skill> skillList = new LinkedList<>();
                skillList.add(skill);
                skillList.add(skill1);
                skillList.add(skill2);

                DeveloperDAO.addDeveloper(connection, "Mario", 9, 1, 5000, skillList);

            } catch (SQLException e){
                logger.error("Error");
            }

        } catch (IOException | SQLException | PropertyVetoException e) {
            logger.error("No connections provided");
        }

    }

}
