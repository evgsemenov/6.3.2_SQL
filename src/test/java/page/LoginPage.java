package page;

import com.codeborne.selenide.SelenideElement;
import data.DataGenerator;
import data.DataHelper;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private SelenideElement loginField = $("[data-test-id='login'] input");
  private SelenideElement passwordField = $("[data-test-id='password'] input");
  private SelenideElement loginButton = $("[data-test-id='action-login']");
  private SelenideElement errorMessage = $("[data-test-id='error-notification']");
  private SelenideElement inputSubLogin = $("[data-test-id='login']").$("[class='input__sub']");
  private SelenideElement inputSubPassword = $("[data-test-id='password']").$("[class='input__sub']");

  public VerificationPage validLogin(DataHelper.AuthInfo info) {
    loginField.setValue(info.getLogin());
    passwordField.setValue(info.getPassword());
    loginButton.click();
    return new VerificationPage();
  }

  public LoginPage invalidLogin() {
    loginField.setValue(DataGenerator.getRandomLogin());
    passwordField.setValue(DataHelper.getAuthInfo().getPassword());
    loginButton.click();
    errorMessage.shouldBe(visible).shouldHave(exactText("Ошибка\n" +
            "Ошибка! Неверно указан логин или пароль"));
    return new LoginPage();
  }

  public LoginPage invalidPassword() {
    loginField.setValue(DataHelper.getAuthInfo().getLogin());
    passwordField.setValue(DataGenerator.getRandomPassword());
    loginButton.click();
    errorMessage.shouldBe(visible).shouldHave(exactText("Ошибка\n" +
            "Ошибка! Неверно указан логин или пароль"));
    return new LoginPage();
  }

  public LoginPage emptyFieldsSent() {
    loginButton.click();
    inputSubLogin.shouldBe(visible).shouldHave(exactText("Поле обязательно для заполнения"));
    inputSubPassword.shouldBe(visible).shouldHave(exactText("Поле обязательно для заполнения"));
    return new LoginPage();
  }
}