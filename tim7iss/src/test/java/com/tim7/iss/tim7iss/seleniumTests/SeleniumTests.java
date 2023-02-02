package com.tim7.iss.tim7iss.seleniumTests;

import com.tim7.iss.tim7iss.CustomTestData;
import com.tim7.iss.tim7iss.seleniumTests.pages.UnregisteredUserPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeleniumTests {

    public WebDriver driver;
    public CustomTestData customTestData = new CustomTestData();

    @BeforeEach
    public void initializeWebDriver() throws IOException {

        String driverType = "webdriver.chrome.driver";
        String driverLocation = "C:\\Users\\ivanm\\OneDrive\\Desktop\\Testirnaje softvera kolokvijum\\chromedriver.exe";
        System.setProperty(driverType, driverLocation);

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }

    @Test
    public void shouldPassengerLogin() throws InterruptedException, IOException {
        String email = customTestData.getPassenger1().getEmailAddress();
        String password = "Petar123";
        String passengerHomePageUrl = "http://localhost:4200/passenger-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(passengerHomePageUrl));
        driver.quit();
    }

    @Test
    public void shouldDriverLogin() throws IOException {
        String email = customTestData.getDriver().getEmailAddress();
        String password = "Mika1234";
        String driverHomePageUrl = "http://localhost:4200/driver-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(driverHomePageUrl));
        driver.quit();
    }

    @Test
    public void shouldAdminLogin() throws IOException {
        String email = customTestData.getAdmin().getEmailAddress();
        String password = "Admin123";
        String adminHomePageUrl = "http://localhost:4200/admin-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(adminHomePageUrl));
        driver.quit();

    }

    @Test
    public void wrongLogin() {
        String email = "wrongEmail@email.com";
        String password = "wrongPassword";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        Assertions.assertEquals("Invalid email or password", unregisteredUserPage.getErrorMessage());
        driver.quit();
    }

    @Test
    public void wrongEmailLogin() {
        String email = "wrongEmail@email.com";
        String password = "Petar123";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        Assertions.assertEquals("Invalid email or password", unregisteredUserPage.getErrorMessage());
        driver.quit();
    }

    @Test
    public void wrongPasswordLogin() throws IOException {
        String email = customTestData.getPassenger1().getEmailAddress();
        String password = "Petar123456";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        Assertions.assertEquals("Invalid email or password", unregisteredUserPage.getErrorMessage());
        driver.quit();
    }

    @Test
    public void emptyEmail() {
        String email = "";
        String password = "wrongPassword";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        Assertions.assertFalse(unregisteredUserPage.isEmailValid());
        driver.quit();
    }

    @Test
    public void emptyPassword() {
        String email = "wrongEmail@email.com";
        String password = "";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        Assertions.assertFalse(unregisteredUserPage.isPasswordValid());
        driver.quit();
    }

    @Test
    public void emptyEmailAndPassword() {
        String email = "";
        String password = "";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        Assertions.assertFalse(unregisteredUserPage.isEmailValid());
        Assertions.assertFalse(unregisteredUserPage.isPasswordValid());

        driver.quit();
    }
}
