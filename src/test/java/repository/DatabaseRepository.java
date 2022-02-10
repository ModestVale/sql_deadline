package repository;

import java.sql.*;

public class DatabaseRepository {
    private final String getLastCodeSqlFormat = "SELECT ac.code FROM auth_codes ac \n" +
            "JOIN users u ON ac.user_id = u.id \n" +
            "WHERE u.login ='%s'\n" +
            "ORDER BY  ac.created DESC \n" +
            "LIMIT 1";

    public String getLastCode(String userName) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
            Statement statement = connection.createStatement();
            var getLastCodeSql = String.format(getLastCodeSqlFormat, userName);
            ResultSet resultSet = statement.executeQuery(getLastCodeSql);
            while (resultSet.next()) {
                String code = resultSet.getString(1);
                return code;
            }
        } catch (SQLException exception) {
            System.out.println(exception);
        }
        return "";
    }

    public void clearDb() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
            Statement statement = connection.createStatement();
            statement.execute("delete from auth_codes");
            statement.execute("delete from cards");
            statement.execute("delete from users");
        } catch (SQLException exception) {
            System.out.println(exception);
        }
    }
}
