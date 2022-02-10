package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {

    private final SelenideElement dashboard = $("[data-test-id='dashboard']");

    public DashboardPage() {
        dashboard.shouldHave(Condition.text("  Личный кабинет"), Duration.ofSeconds(15));
    }
}
