package DAO;

import org.apache.commons.dbcp.BasicDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static DataSource datasource;
    private BasicDataSource ds;

    private DataSource() throws IOException, SQLException, PropertyVetoException {
        ds = new BasicDataSource();

        String url = "jdbc:mysql://localhost:3306/task?useSSL=false";
        String user = "root";
        String password = "root";

        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername(user);
        ds.setPassword(password);
        ds.setUrl(url);

        ds.setMinIdle(5);
        ds.setMaxIdle(20);
        ds.setMaxOpenPreparedStatements(180);
        ds.setDefaultAutoCommit(false);

    }

    public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) datasource = new DataSource();

        return datasource;
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }

}