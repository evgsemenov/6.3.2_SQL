package databasehelper;

import data.DataGenerator;
import lombok.Data;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.DriverManager;
import java.sql.SQLException;
@Data

public class DatabaseHelper {
//    String userID = String.valueOf(getUserIdByLogin(String login));


    public static void createRandomUser() throws SQLException {
        var runner = new QueryRunner();
        var dataSQL ="INSERT INTO users(id, login, password) VALUES (?, ?, ?);";
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass"            );
        ) {
            runner.update(conn, dataSQL, DataGenerator.getRandomUserId(), DataGenerator.getRandomLogin(), DataGenerator.getRandomPassword());
        }
    }

    public static int getUserIdByLogin(String login) throws SQLException{
        var runner = new QueryRunner();
        var userSQL = "SELECT id FROM users WHERE login='" + login + "';";
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass"            );
        ) {
            var userID = runner.execute(conn, userSQL);
            return userID;
        }
    }

    public static int getAuthCodeByLogin(String login) throws SQLException {
         var runner = new QueryRunner();
         var authSQL = "SELECT code FROM auth_codes WHERE user_id='" + getUserIdByLogin(login) + "' ORDER BY id DESC LIMIT 1;";
         try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass");
         ) {
             var authCode = runner.execute(conn, authSQL);
             return authCode;
         }
     }
}