package LoginTest;

import Data.DataGenerator;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class InternetBankLoginTest {

    @BeforeAll
    @SneakyThrows
    static void randomUserSetup() {
        var runner = new QueryRunner();
        var dataSQL ="INSERT INTO users(id, login, password) VALUES (?, ?, ?);";
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass"            );
        ) {
            runner.update(conn, dataSQL, 3, DataGenerator.getRandomLogin(), DataGenerator.getRandomPassword());
        }
    }

    @BeforeEach
    void sqlSetup(){
        var userLogin = "SELECT login FROM users WHERE id=3;";
        var userPassword = "SELECT password FROM users WHERE id=3;";
        var userAuthCode = "SELECT code FROM auth_codes WHERE id=3;";
        var runner = new QueryRunner();
    }
    @BeforeEach
    void browserSetup() {
        open("http://localhost:9999");
    }

    @Test
    @Disabled
    void shouldLoginActiveUserWithAuthCode(){
        $("[data-test-id='login']").$("[class='input__control']").setValue("vasya");
        $("[data-test-id='password']").$("[class='input__control']").setValue("qwerty123");
        $("[data-test-id='action-login']").click();
        $("[id='root']").$("[class='paragraph paragraph_theme_alfa-on-white'")
                .shouldHave(exactText("Необходимо подтверждение")).shouldBe(visible);
//        $("[data-test-id='code']").setValue();
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='dashboard']").shouldHave(exactText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfWrongPassword(){
        $("[data-test-id='login']").$("[class='input__control']").setValue("vasya");
        $("[data-test-id='password']").$("[class='input__control']").setValue(DataGenerator.getRandomPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(visible)
                .shouldHave(exactText("Ошибка\n" + "Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldGetErrorIfWrongLogin(){
        $("[data-test-id='login']").$("[class='input__control']").setValue(DataGenerator.getRandomLogin());
        $("[data-test-id='password']").$("[class='input__control']").setValue("qwerty123");
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(visible)
                .shouldHave(exactText("Ошибка\n" + "Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldRequireFilledFieldsIfEmptyLoginPasswordFields(){
        $("[data-test-id='action-login']").click();
        $("[data-test-id='login']").$("[class='input__sub']").shouldBe(visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));
        $("[data-test-id='password']").$("[class='input__sub']").shouldBe(visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));

    }

    @Test
//  Тест почему-то падает с ошибкой 'Actual value: visible:false', хотя в интерфейсе элемент виден"
    void shouldGetErrorIfWrondAuthCode() {
        $("[data-test-id='login']").$("[class='input__control']").setValue("vasya");
        $("[data-test-id='password']").$("[class='input__control']").setValue("qwerty123");
        $("[data-test-id='action-login']").click();
        $("[data-test-id='code']").$("[class='input__control']").setValue(DataGenerator.getRandomAuthCode());
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='error-notification']").shouldBe(visible)
                .shouldHave(exactText("Ошибка\n" + "Ошибка! Неверно указан код! Попробуйте ещё раз."));
    }

    @Test
    void shouldRequireFilledFieldIfEmptyAuthCodeField() {
        $("[data-test-id='login']").$("[class='input__control']").setValue("vasya");
        $("[data-test-id='password']").$("[class='input__control']").setValue("qwerty123");
        $("[data-test-id='action-login']").click();
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='code']").$("[class='input__sub']").shouldBe(visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

}