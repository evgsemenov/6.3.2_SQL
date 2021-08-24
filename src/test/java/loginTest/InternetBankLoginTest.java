package loginTest;

import data.DataGenerator;
import data.DataHelper;
import database.DatabaseManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class InternetBankLoginTest {

    private void successAutorization(){
        open("http://localhost:9999");
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

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
        successAutorization();
    }

    @Test
    void shouldLoginAfterThreeSuccessAutorizationsTest() {
        successAutorization();
        successAutorization();
        successAutorization();
        successAutorization();
    }

    @Test
    void shouldGetErrorIfWrongAuthCode() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());
        verificationPage.invalidVerify();
    }

    @Test
    void shouldGetErrorIfWrongAuthCodeSentThreeTimes() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());
        verificationPage.invalidVerifyThreeTimes();
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        LoginPage loginPage = new LoginPage();
        loginPage.invalidLogin(DataGenerator.getRandomLogin(), DataHelper.getAuthInfo().getPassword());
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        LoginPage loginPage = new LoginPage();
        loginPage.invalidLogin(DataHelper.getAuthInfo().getLogin(), DataGenerator.getRandomPassword());
    }

    @Test
    void shouldRequireFilledFieldsIfEmptyLoginPasswordFields() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        loginPage.sendEmptyField();
    }

    @Test
    void shouldRequireFilledFieldIfEmptyAuthCodeField() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());
        verificationPage.sendEmptyField();
    }
}
