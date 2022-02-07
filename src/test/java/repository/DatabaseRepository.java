package repository;

import java.sql.*;

public class DatabaseRepository {
    private String getLastCodeSql = "SELECT ac.code FROM auth_codes ac \n" +
            "JOIN users u ON ac.user_id = u.id \n" +
            "WHERE u.login ='vasya'\n" +
            "ORDER BY  ac.created DESC \n" +
            "LIMIT 1";

    public String getLastCode() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(getLastCodeSql);
        while (resultSet.next()) {
            String code = resultSet.getString(1);
            return code;
        }
        return "";
    }

    public  void clearDb() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
        Statement statement = connection.createStatement();
        statement.execute("delete from auth_codes");
        statement.execute("delete from cards");
        statement.execute("delete from users");
    }
}
