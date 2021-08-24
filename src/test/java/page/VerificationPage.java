package page;

import com.codeborne.selenide.SelenideElement;
import data.DataGenerator;
import data.DataHelper;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.Keys.CONTROL;
import static org.openqa.selenium.Keys.DELETE;

public class VerificationPage {
  private SelenideElement codeField = $("[data-test-id=code] input");
  private SelenideElement verifyButton = $("[data-test-id=action-verify]");
  private SelenideElement errorMessage = $("[data-test-id='error-notification']");
  private SelenideElement input_sub = $("[data-test-id='code']").$("[class='input__sub']");

  public VerificationPage() {
    codeField.shouldBe(visible);
  }

  public DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
    codeField.setValue(verificationCode.getCode());
    verifyButton.click();
    return new DashboardPage();
  }

  public VerificationPage invalidVerify() {
    codeField.setValue(DataGenerator.getRandomAuthCode());
    verifyButton.click();
    errorMessage.shouldBe(visible).shouldHave(exactText("Ошибка\n" +
            "Ошибка! Неверно указан код! Попробуйте ещё раз."));
    return new VerificationPage();
  }

  public LoginPage invalidVerifyThreeTimes() {
    invalidVerify();
    invalidVerify();
    codeField.setValue(DataGenerator.getRandomAuthCode());
    verifyButton.click();
    errorMessage.shouldBe(visible).shouldHave(exactText("Ошибка!\n" +
            "Ошибка! Превышено количество попыток ввода кода!"));
    return new LoginPage();
  }

  public VerificationPage sendEmptyField() {
    codeField.sendKeys(CONTROL + "A", DELETE);
    verifyButton.click();
    $("[data-test-id='code']").$("[class='input__sub']").shouldBe(visible).
            shouldHave(exactText("Поле обязательно для заполнения"));
    return new VerificationPage();
  }
}
