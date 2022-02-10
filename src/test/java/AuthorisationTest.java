import domain.UserInfo;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.LoginPage;
import repository.DatabaseRepository;
import repository.TestDataGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.open;


public class AuthorisationTest {
    private Process process;
    private final TestDataGenerator testDataGenerator;
    private final DatabaseRepository repository;

    public AuthorisationTest() {
        repository = new DatabaseRepository();
        testDataGenerator = new TestDataGenerator();
    }

    @BeforeEach
    public void startup() throws IOException, InterruptedException {
        clearDatabase();
        Path path = Paths.get("app-deadline.jar");
        Path path1 = path.toAbsolutePath();

        process = new ProcessBuilder("java", "-jar", path1.toString()).start();
        Thread.sleep(3000);
    }

    @AfterEach
    public void clearDatabase() {
        if (process != null) {
            process.destroy();
        }
        repository.clearDb();
    }

    @Test
    public void shouldAuthorisationIfValidData() {
        UserInfo userInfo = testDataGenerator.getUserInfo(false, false);

        val loginPage = open("http://localhost:9999/", LoginPage.class);
        val verificationPage = loginPage.validLogin(userInfo);
        String code = repository.getLastCode("vasya");
        val dashboardPage = verificationPage.validCode(code);
    }

    @Test
    public void shouldNotAuthorisationIfPasswordInvalid() {
        UserInfo userInfo = testDataGenerator.getUserInfo(true, false);

        val loginPage = open("http://localhost:9999/", LoginPage.class);
        loginPage.inputInvalidPassword(
                userInfo,
                "Ошибка\nОшибка! Неверно указан логин или пароль");
    }

    @Test
    public void shouldNotAuthorisationIfUsernameInvalid() {
        UserInfo userInfo = testDataGenerator.getUserInfo(false, true);
        val loginPage = open("http://localhost:9999/", LoginPage.class);
        loginPage.inputInvalidPassword(
                userInfo,
                "Ошибка\nОшибка! Неверно указан логин или пароль");
    }

    @Test
    public void shouldNotAuthorisationIfCodeInvalid() {
        UserInfo userInfo = testDataGenerator.getUserInfo(false, false);

        val loginPage = open("http://localhost:9999/", LoginPage.class);
        val verificationPage = loginPage.validLogin(userInfo);
        val code = testDataGenerator.getInvalidCode();
        verificationPage.invalidCode(code, "Ошибка\nОшибка! Неверно указан код! Попробуйте ещё раз.");
    }
}
