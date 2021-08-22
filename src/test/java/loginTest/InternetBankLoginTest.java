package loginTest;

import data.DataHelper;
import database.DatabaseManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import page.LoginPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;

public class InternetBankLoginTest {
    @BeforeEach
    void browserSetup() throws InterruptedException {
        Thread.sleep(3000);
    open("http://localhost:9999");
    }

    @AfterAll
    static void dbTearDown() {
        DatabaseManager.clearDatabase();
    }

    @Test
    void shouldLoginActiveUserWithAuthCode() throws SQLException {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldGetErrorIfWrongAuthCode() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.invalidVerify();
    }

    @Test
    void shouldGetErrorIfWrongAuthCodeSentThreeTimes() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.invalidVerifyThreeTimes();
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.invalidLogin(authInfo);
        verificationPage.invalidLogin(authInfo);
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.invalidPassword(authInfo);
        verificationPage.invalidPassword(authInfo);
    }

    @Test
    void shouldRequireFilledFieldsIfEmptyLoginPasswordFields() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        loginPage.emptyFieldsSent();
    }

    @Test
    void shouldRequireFilledFieldIfEmptyAuthCodeField() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.sendEmptyField();
    }
}
