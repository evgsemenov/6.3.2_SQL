package database;

import data.DataGenerator;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    public static void createRandomUser() throws SQLException {
        var runner = new QueryRunner();
        var dataSQL = "INSERT INTO users(id, login, password) VALUES (?, ?, ?);";
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass");
        ) {
            runner.update(conn, dataSQL, DataGenerator.getRandomUserId(), DataGenerator.getRandomLogin(), DataGenerator.getRandomPassword());
        }
    }

    public static String getUserIdByLogin(String login) throws SQLException {
        var runner = new QueryRunner();
        var userSQL = "SELECT id FROM users WHERE login='" + login + "';";
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass");
        ) {
            var userResult = runner.query(conn, userSQL, new ScalarHandler<>());
            String userID = userResult.toString();
            return userID;
        }
    }

    public static String getAuthCodeByLogin(String login) {
        var runner = new QueryRunner();
        var authSQL = "SELECT code FROM auth_codes WHERE created = (SELECT max(created) FROM auth_codes);";
        String authCode = null;
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass");
        ) {
            var authResult = runner.query(conn, authSQL, new ScalarHandler<>());
            authCode = authResult.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authCode;
    }

    public static String getUserStatusByLogin(String login) {
        var runner = new QueryRunner();
        var statusSQL = "SELECT status FROM users WHERE login='" + login + "';";
        String status = null;
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass");
        ) {
            var statusResult = runner.query(conn, statusSQL, new ScalarHandler<>());
            statusSQL = statusResult.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static void clearDatabase() {
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass");
             Statement stmt = conn.createStatement();) {
            var disableChecks = "SET FOREIGN_KEY_CHECKS = 0;";
            var truncateAuth = "TRUNCATE TABLE auth_codes;";
            var truncateTransactions = "TRUNCATE TABLE card_transactions;";
            var truncateCards = "TRUNCATE TABLE cards;";
            var truncateUsers = "TRUNCATE TABLE users;";
            var activateChecks = "SET FOREIGN_KEY_CHECKS = 1;";
            stmt.executeUpdate(disableChecks);
            stmt.executeUpdate(truncateUsers);
            stmt.executeUpdate(truncateCards);
            stmt.executeUpdate(truncateAuth);
            stmt.executeUpdate(activateChecks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}