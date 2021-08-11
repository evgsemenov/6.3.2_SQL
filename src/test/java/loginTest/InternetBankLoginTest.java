package loginTest;

import data.DataGenerator;
import databasehelper.DatabaseHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class InternetBankLoginTest {

    String login = "vasya";
    String password = "qwerty123";

    @BeforeEach
    void browserSetup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLoginActiveUserWithAuthCode() throws SQLException {
        $("[data-test-id='login']").$("[class='input__control']").setValue(login);
        $("[data-test-id='password']").$("[class='input__control']").setValue(password);
        $("[data-test-id='action-login']").click();
        $("[id='root']").$("[class='paragraph paragraph_theme_alfa-on-white'")
                .shouldHave(exactText("Необходимо подтверждение")).shouldBe(visible);
        $("[data-test-id='code']").$("[class='input__control']").
                setValue(String.valueOf(DatabaseHelper.getAuthCodeByLogin(login)));
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='dashboard']").shouldHave(exactText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        $("[data-test-id='login']").$("[class='input__control']").setValue(login);
        $("[data-test-id='password']").$("[class='input__control']").setValue(DataGenerator.getRandomPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(visible)
                .shouldHave(exactText("Ошибка\n" + "Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        $("[data-test-id='login']").$("[class='input__control']").setValue(DataGenerator.getRandomLogin());
        $("[data-test-id='password']").$("[class='input__control']").setValue(password);
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
        $("[data-test-id='login']").$("[class='input__control']").setValue(login);
        $("[data-test-id='password']").$("[class='input__control']").setValue(password);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='code']").$("[class='input__control']").setValue(DataGenerator.getRandomAuthCode());
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='error-notification']").shouldBe(visible)
                .shouldHave(exactText("Ошибка\n" + "Ошибка! Неверно указан код! Попробуйте ещё раз."));
    }

    @Test
    void shouldRequireFilledFieldIfEmptyAuthCodeField() {
        $("[data-test-id='login']").$("[class='input__control']").setValue(login);
        $("[data-test-id='password']").$("[class='input__control']").setValue(password);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='code']").$("[class='input__sub']").shouldBe(visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }
}
