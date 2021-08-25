package loginTest;

import data.DataGenerator;
import data.DataHelper;
import database.DatabaseManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

public class InternetBankLoginTest {

    private void successAuthorization(){
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
    void shouldGetErrorIfWrongLoginTest() {
        LoginPage loginPage = new LoginPage();
        loginPage.invalidLogin(DataGenerator.getRandomLogin(), DataHelper.getAuthInfo().getPassword());
    }

    @Test
    void shouldGetErrorIfWrongPasswordTest() {
        LoginPage loginPage = new LoginPage();
        loginPage.invalidLogin(DataHelper.getAuthInfo().getLogin(), DataGenerator.getRandomPassword());
    }

    @Test
    void shouldBlockUserAfterThreeWrongPasswordsTest(){
        LoginPage loginPage = new LoginPage();
        loginPage.invalidLogin(DataHelper.getAuthInfo().getLogin(), DataGenerator.getRandomPassword());
        loginPage.invalidLogin(DataHelper.getAuthInfo().getLogin(), DataGenerator.getRandomPassword());
        loginPage.getBlockMessage(DataHelper.getAuthInfo().getLogin(), DataGenerator.getRandomPassword());
        assertThat(DatabaseManager.getUserStatusByLogin(DataHelper.getAuthInfo().getLogin()), not("active"));
    }

    @Test
    void shouldRequireFilledFieldsIfEmptyLoginPasswordFieldsTest() {
        LoginPage loginPage = new LoginPage();
        loginPage.sendEmptyField();
    }

    @Test
    void shouldRequireFilledFieldIfEmptyAuthCodeFieldTest() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());
        verificationPage.sendEmptyField();
    }

    @Test
    void shouldGetErrorIfWrongAuthCodeTest() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());
        verificationPage.invalidVerify(DataGenerator.getRandomAuthCode());
    }

    @Test
    void shouldGetErrorIfWrongAuthCodeSentThreeTimesTest() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());
        verificationPage.invalidVerify(DataGenerator.getRandomAuthCode());
        verificationPage.invalidVerify(DataGenerator.getRandomAuthCode());
        verificationPage.getBlockMessage(DataGenerator.getRandomAuthCode());
    }

    @Test
    void shouldLoginActiveUserWithAuthCodeTest() {
        successAuthorization();
    }

    @Test
    void shouldLoginAfterThreeSuccessAuthorizationsTest() {
        successAuthorization();
        successAuthorization();
        successAuthorization();
        successAuthorization();
    }
}
