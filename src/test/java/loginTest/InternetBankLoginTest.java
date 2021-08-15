package loginTest;

import data.DataGenerator;
import data.UsersInfo;
import databasehelper.DatabaseManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
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
    void shouldLoginActiveUserWithAuthCode() throws SQLException {
        $("[data-test-id='login']").$("[class='input__control']").setValue(UsersInfo.activeUserLogin);
        $("[data-test-id='password']").$("[class='input__control']").setValue(UsersInfo.activeUserPassword);
        $("[data-test-id='action-login']").click();
        $("[id='root']").$("[class='paragraph paragraph_theme_alfa-on-white'")
                .shouldHave(exactText("Необходимо подтверждение")).shouldBe(visible);
        $("[data-test-id='code']").$("[class='input__control']").
                setValue(DatabaseManager.getAuthCodeByLogin(UsersInfo.activeUserLogin));
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='dashboard']").shouldBe(visible).shouldHave(exactText("Личный кабинет"));
    }

    @Test
    void shouldGetErrorIfWrongAuthCodeSentThreeTimes() {
        $("[data-test-id='login']").$("[class='input__control']").setValue(UsersInfo.activeUserLogin);
        $("[data-test-id='password']").$("[class='input__control']").setValue(UsersInfo.activeUserPassword);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='code']").$("[class='input__control']")
                .setValue(DataGenerator.getRandomAuthCode());
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='code']").$("[class='input__control']")
                .setValue(DataGenerator.getRandomAuthCode());
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='code']").$("[class='input__control']")
                .setValue(DataGenerator.getRandomAuthCode());
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='error-notification']").shouldBe(visible).
                shouldHave(exactText("Ошибка\n" + "Ошибка! Превышено количество попыток ввода кода!"));
    }

    @Test
    void shouldReturnToMainPageIfWrongAuthCodeSentThreeTimes() {
        $("[data-test-id='login']").$("[class='input__control']").setValue(UsersInfo.activeUserLogin);
        $("[data-test-id='password']").$("[class='input__control']").setValue(UsersInfo.activeUserPassword);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='code']").$("[class='input__control']")
                .setValue(DataGenerator.getRandomAuthCode());
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='code']").$("[class='input__control']")
                .setValue(DataGenerator.getRandomAuthCode());
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='code']").$("[class='input__control']")
                .setValue(DataGenerator.getRandomAuthCode());
        $("[data-test-id='action-verify']").click();
        $("[id='root']").$("[class='paragraph paragraph_theme_alfa-on-white']").shouldBe(visible)
                .shouldHave(exactText("Мы гарантируем безопасность ваших данных"));
    }


    @Test
    void shouldGetErrorIfWrongPassword() {
        $("[data-test-id='login']").$("[class='input__control']").setValue(UsersInfo.activeUserLogin);
        $("[data-test-id='password']").$("[class='input__control']").setValue(DataGenerator.getRandomPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(visible)
                .shouldHave(exactText("Ошибка\n" + "Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        $("[data-test-id='login']").$("[class='input__control']").setValue(DataGenerator.getRandomLogin());
        $("[data-test-id='password']").$("[class='input__control']").setValue(UsersInfo.activeUserPassword);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(visible)
                .shouldHave(exactText("Ошибка\n" + "Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldRequireFilledFieldsIfEmptyLoginPasswordFields() {
        $("[data-test-id='action-login']").click();
        $("[data-test-id='login']").$("[class='input__sub']").shouldBe(visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));
        $("[data-test-id='password']").$("[class='input__sub']").shouldBe(visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));

    }

    @Test
    void shouldGetErrorIfWrondAuthCode() {
        $("[data-test-id='login']").$("[class='input__control']").setValue(UsersInfo.activeUserLogin);
        $("[data-test-id='password']").$("[class='input__control']").setValue(UsersInfo.activeUserPassword);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='code']").$("[class='input__control']").setValue(DataGenerator.getRandomAuthCode());
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='error-notification']").shouldBe(visible)
                .shouldHave(exactText("Ошибка\n" + "Ошибка! Неверно указан код! Попробуйте ещё раз."));
    }

    @Test
    void shouldRequireFilledFieldIfEmptyAuthCodeField() {
        $("[data-test-id='login']").$("[class='input__control']").setValue(UsersInfo.activeUserLogin);
        $("[data-test-id='password']").$("[class='input__control']").setValue(UsersInfo.activeUserPassword);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='code']").$("[class='input__sub']").shouldBe(visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }
}
