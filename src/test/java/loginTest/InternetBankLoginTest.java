package loginTest;

import data.DataHelper;
import database.DatabaseManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class InternetBankLoginTest {

    @BeforeEach
    void browserSetup() {
    open("http://localhost:9999");
    }

    @AfterAll
    static void dbTearDown() {
        DatabaseManager.clearDatabase();
    }

    @Test
    void shouldLoginActiveUserWithAuthCode() {
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
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.invalidLogin();
        verificationPage.invalidLogin();
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.invalidPassword();
        verificationPage.invalidPassword();
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
