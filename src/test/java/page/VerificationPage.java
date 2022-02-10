package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class VerificationPage {
    private final SelenideElement codeInput = $("[data-test-id=code] input");

    @FindBy(css = "[data-test-id='action-verify']")
    private SelenideElement verifyButton;
    @FindBy(css = "[data-test-id='error-notification']")
    private SelenideElement errorNotification;

    public VerificationPage() {
        codeInput.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public DashboardPage validCode(String code) {
        codeInput.setValue(code);
        verifyButton.click();

        return page(DashboardPage.class);
    }

    public void invalidCode(String code, String text) {
        codeInput.setValue(code);
        verifyButton.click();
        errorNotification.shouldBe(Condition.visible, Duration.ofSeconds(15));
        errorNotification.shouldHave(Condition.text(text), Duration.ofSeconds(3));
    }
}
