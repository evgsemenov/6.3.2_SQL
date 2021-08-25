package loginTest;

import data.DataGenerator;
import data.DataHelper;
import database.DatabaseManager;
import org.junit.jupiter.api.*;
import page.LoginPage;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InternetBankLoginTest {

    private void successAuthorization(){
        open("http://localhost:9999");
        var authInfo = DataHelper.getAuthInfo2();
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
    @Order(1)
    void shouldGetErrorIfWrongLoginTest() {
        LoginPage loginPage = new LoginPage();
        loginPage.invalidLogin(DataGenerator.getRandomLogin(), DataHelper.getAuthInfo().getPassword());
    }

    @Test
    @Order(2)
    void shouldGetErrorIfWrongPasswordTest() {
        LoginPage loginPage = new LoginPage();
        loginPage.invalidLogin(DataHelper.getAuthInfo().getLogin(), DataGenerator.getRandomPassword());
    }

    @Test
    @Order(3)
    void shouldBlockUserAfterThreeWrongPasswordsTest(){
        LoginPage loginPage = new LoginPage();
        loginPage.invalidLogin(DataHelper.getAuthInfo().getLogin(), DataGenerator.getRandomPassword());
        loginPage.invalidLogin(DataHelper.getAuthInfo().getLogin(), DataGenerator.getRandomPassword());
        loginPage.getBlockMessage(DataHelper.getAuthInfo().getLogin(), DataGenerator.getRandomPassword());
        var actual = DatabaseManager.getUserStatusByLogin(DataHelper.getAuthInfo().getLogin());
        assertNotEquals(actual, "active");
    }

    @Test
    @Order(4)
    void shouldRequireFilledFieldsIfEmptyLoginPasswordFieldsTest() {
        LoginPage loginPage = new LoginPage();
        loginPage.sendEmptyField();
    }

    @Test
    @Order(5)
    void shouldRequireFilledFieldIfEmptyAuthCodeFieldTest() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());
        verificationPage.sendEmptyField();
    }

    @Test
    @Order(6)
    void shouldGetErrorIfWrongAuthCodeTest() {
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());
        verificationPage.invalidVerify(DataGenerator.getRandomAuthCode());
    }

    @Test
    @Order(7)
    void shouldGetErrorIfWrongAuthCodeSentThreeTimesTest() {
        var authInfo = DataHelper.getAuthInfo2();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());
        verificationPage.invalidVerify(DataGenerator.getRandomAuthCode());
        verificationPage.invalidVerify(DataGenerator.getRandomAuthCode());
        verificationPage.getBlockMessage(DataGenerator.getRandomAuthCode());
        var actual = DatabaseManager.getUserStatusByLogin(DataHelper.getAuthInfo().getLogin());
        assertNotEquals(actual, "active");
    }

    @Test
    @Order(8)
    void shouldLoginActiveUserWithAuthCodeTest() {
        successAuthorization();
    }

    @Test
    @Order(9)
    void shouldLoginAfterThreeSuccessAuthorizationsTest() {
        successAuthorization();
        successAuthorization();
        successAuthorization();
        successAuthorization();
    }
}
