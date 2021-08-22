package data;

import database.DatabaseManager;
import lombok.Value;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;


public class DataHelper {
  private DataHelper() {
  }

  @Value
  public static class AuthInfo {
    private String login;
    private String password;
  }

  public static AuthInfo getAuthInfo() {
    return new AuthInfo("vasya", "qwerty123");
  }

  @Value
  public static class VerificationCode {
    private String code;
  }

  public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) throws SQLException {
      return new VerificationCode(DatabaseManager.getAuthCodeByLogin("vasya"));
    }
  }

