import com.codeborne.selenide.Condition;
import domain.UserInfo;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.DatabaseRepository;
import repository.TestDataGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class AuthorisationTest {


    private Process process;
    private TestDataGenerator testDataGenerator;
    private DatabaseRepository repository;

    public AuthorisationTest()
    {
        repository = new DatabaseRepository();
        testDataGenerator = new TestDataGenerator();
    }

    @BeforeEach
    public void startup() throws IOException, SQLException {
        clearDatabase();
        Path path = Paths.get("app-deadline.jar");
        Path path1 = path.toAbsolutePath();

        process = new ProcessBuilder("java", "-jar", path1.toString()).start();
        open("http://localhost:9999/");
    }

    @AfterEach
    public void clearDatabase() throws SQLException {
        if(process != null) {
            process.destroy();
        }
        repository.clearDb();
    }

    @Test
    public void shouldAuthorisationIfValidData() throws SQLException {
        UserInfo userInfo = testDataGenerator.getUserInfo();
        $("[data-test-id='login'] input").setValue(userInfo.getUserName());
        $("[data-test-id='password'] input").setValue(userInfo.getPassword());
        $("[data-test-id='action-login']").click();

        $(".paragraph_theme_alfa-on-white").shouldHave(Condition.text("  Необходимо подтверждение"), Duration.ofSeconds(15));


        String code = repository.getLastCode();
        $("[data-test-id='code'] input").setValue(code);
        $("[data-test-id='action-verify']").click();

        $("[data-test-id='dashboard']").shouldHave(Condition.text("  Личный кабинет"), Duration.ofSeconds(15));

    }

    @Test
    public void shouldNotAuthorisationIfPasswordInvalid() {
        UserInfo userInfo = testDataGenerator.getUserInfo();
        $("[data-test-id='login'] input").setValue(userInfo.getUserName());
        $("[data-test-id='password'] input").setValue(testDataGenerator.getInvalidPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='error-notification']")
                .shouldHave(Condition.text("Ошибка\nОшибка! Неверно указан логин или пароль"), Duration.ofSeconds(3));
    }

    @Test
    public void shouldNotAuthorisationIfUsernameInvalid() {
        UserInfo userInfo = testDataGenerator.getUserInfo();
        $("[data-test-id='login'] input").setValue(testDataGenerator.getInvalidUserName());
        $("[data-test-id='password'] input").setValue(userInfo.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='error-notification']")
                .shouldHave(Condition.text("Ошибка\nОшибка! Неверно указан логин или пароль"), Duration.ofSeconds(3));
    }

    @Test
    public void shouldNotAuthorisationIfCodeInvalid() {
        UserInfo userInfo = testDataGenerator.getUserInfo();
        $("[data-test-id='login'] input").setValue(userInfo.getUserName());
        $("[data-test-id='password'] input").setValue(userInfo.getPassword());
        $("[data-test-id='action-login']").click();

        $(".paragraph_theme_alfa-on-white").shouldHave(Condition.text("  Необходимо подтверждение"), Duration.ofSeconds(15));

        $("[data-test-id='code'] input").setValue(testDataGenerator.getInvalidCode());
        $("[data-test-id='action-verify']").click();

        $("[data-test-id='error-notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='error-notification']")
                .shouldHave(Condition.text("Ошибка\nОшибка! Неверно указан код! Попробуйте ещё раз."), Duration.ofSeconds(15));
    }
}
