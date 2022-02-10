package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import domain.UserInfo;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.page;

public class LoginPage {
    @FindBy(css = "[data-test-id='login'] input")
    private SelenideElement loginField;
    @FindBy(css = "[data-test-id='password'] input")
    private SelenideElement passwordField;
    @FindBy(css = "[data-test-id='action-login']")
    private SelenideElement loginButton;
    @FindBy(css = "[data-test-id='error-notification']")
    private SelenideElement errorNotification;

    public VerificationPage validLogin(UserInfo info) {
        loginField.setValue(info.getUserName());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return page(VerificationPage.class);
    }

    public void inputInvalidPassword(UserInfo info, String text) {
        loginField.setValue(info.getUserName());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        errorNotification.shouldBe(Condition.visible, Duration.ofSeconds(15));
        errorNotification.shouldHave(Condition.text(text), Duration.ofSeconds(3));
    }
}
