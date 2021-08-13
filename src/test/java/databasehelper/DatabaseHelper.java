package databasehelper;

import data.DataGenerator;
import lombok.Data;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
@Data

public class DatabaseHelper {

    public static void createRandomUser() throws SQLException {
        var runner = new QueryRunner();
        var dataSQL ="INSERT INTO users(id, login, password) VALUES (?, ?, ?);";
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass"            );
        ) {
            runner.update(conn, dataSQL, DataGenerator.getRandomUserId(), DataGenerator.getRandomLogin(), DataGenerator.getRandomPassword());
        }
    }

    public static String getUserIdByLogin(String login) throws SQLException{
        var runner = new QueryRunner();
        var userSQL = "SELECT id FROM users WHERE login='" + login + "';";
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass"            );
        ) {
            var userResult = runner.query(conn, userSQL, new ScalarHandler<>());
            String userID = userResult.toString();
            return userID;
        }
    }

    public static String getAuthCodeByLogin(String login) throws SQLException {
         var runner = new QueryRunner();
         var authSQL = "SELECT code FROM auth_codes WHERE user_id='" + getUserIdByLogin(login) + "' ORDER BY id DESC LIMIT 1;";
         try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass");
         ) {
             var authResult = runner.query(conn, authSQL, new ScalarHandler<>());
             String authCode = authResult.toString();
             return authCode;
         }
     }
}