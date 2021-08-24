package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.Keys.CONTROL;
import static org.openqa.selenium.Keys.DELETE;

public class LoginPage {
  private SelenideElement loginField = $("[data-test-id='login'] input");
  private SelenideElement passwordField = $("[data-test-id='password'] input");
  private SelenideElement loginButton = $("[data-test-id='action-login']");
  private SelenideElement errorMessage = $("[data-test-id='error-notification']");
  private SelenideElement inputSubLogin = $("[data-test-id='login']").$("[class='input__sub']");
  private SelenideElement inputSubPassword = $("[data-test-id='password']").$("[class='input__sub']");

  private void clearField() {
    loginField.sendKeys(CONTROL + "A", DELETE);
    passwordField.sendKeys(CONTROL + "A", DELETE);
  }

  private void fillAuthInfo(String login, String password) {
    clearField();
    loginField.setValue(login);
    passwordField.setValue(password);
    loginButton.click();
  }

  public VerificationPage validLogin(String login, String password) {
    fillAuthInfo(login, password);
    return new VerificationPage();
  }

  public LoginPage invalidLogin(String login, String password) {
    fillAuthInfo(login, password);
    errorMessage.shouldBe(visible).shouldHave(exactText("Ошибка\n" +
            "Ошибка! Неверно указан логин или пароль"));
    return new LoginPage();
  }

  public LoginPage sendEmptyField() {
    clearField();
    loginButton.click();
    inputSubLogin.shouldBe(visible).shouldHave(exactText("Поле обязательно для заполнения"));
    inputSubPassword.shouldBe(visible).shouldHave(exactText("Поле обязательно для заполнения"));
    return new LoginPage();
  }
}