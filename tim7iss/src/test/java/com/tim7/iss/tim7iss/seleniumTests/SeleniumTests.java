package com.tim7.iss.tim7iss.seleniumTests;

import com.tim7.iss.tim7iss.CustomTestData;
import com.tim7.iss.tim7iss.seleniumTests.pages.DriverActiveRidePage;
import com.tim7.iss.tim7iss.seleniumTests.pages.PassengerActiveRidePage;
import com.tim7.iss.tim7iss.seleniumTests.pages.PassengerHomePage;
import com.tim7.iss.tim7iss.seleniumTests.pages.UnregisteredUserPage;
import org.junit.FixMethodOrder;
import com.tim7.iss.tim7iss.models.User;
import com.tim7.iss.tim7iss.seleniumTests.pages.UnregisteredUserPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SeleniumTests {


    public WebDriver driver;
    public CustomTestData customTestData = new CustomTestData();

    @BeforeEach
    public void initializeWebDriver() throws IOException {

        String driverType = "webdriver.chrome.driver";
        String driverLocation = "src/main/resources/webDrivers/chromedriver.exe";
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

    @Test
    public void scheduleRide1_SchedulingImmediately() {
        String email = "immediate.schedule.success@email.com";
        String password = "Jovan123";
        String passengerHomePageUrl = "http://localhost:4200/passenger-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(passengerHomePageUrl));

        PassengerHomePage passengerHomePage = new PassengerHomePage(driver);
        passengerHomePage.setRideRoutes();
        passengerHomePage.schedule();
        assert passengerHomePage.successfulRideSchedule();
        passengerHomePage.closeDialog();

        driver.quit();
    }

    @Test
    public void scheduleRide2_CannotScheduleIfPassengerHasPendingRide() {
        String email = "immediate.schedule.success@email.com";
        String password = "Jovan123";
        String passengerHomePageUrl = "http://localhost:4200/passenger-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(passengerHomePageUrl));

        PassengerHomePage passengerHomePage = new PassengerHomePage(driver);
        passengerHomePage.setRideRoutes();
        passengerHomePage.schedule();
        assert passengerHomePage.passengerHasPendingRide();
        passengerHomePage.closeDialog();

        driver.quit();
    }

    @Test
    public void scheduleRide3_NotSettingRoutesScheduling() {
        String email = "immediate.schedule.success@email.com";
        String password = "Jovan123";
        String passengerHomePageUrl = "http://localhost:4200/passenger-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(passengerHomePageUrl));

        PassengerHomePage passengerHomePage = new PassengerHomePage(driver);
        passengerHomePage.schedule();
        assert passengerHomePage.notSpecifiedAnyRoutes();
        passengerHomePage.closeDialog();

        driver.quit();
    }

    @Test
    public void scheduleRide4_NoDriversAvailableWhenScheduling() {
        String email = "clearing.routes.schedule@email.com";
        String password = "Jovan123";
        String passengerHomePageUrl = "http://localhost:4200/passenger-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(passengerHomePageUrl));

        PassengerHomePage passengerHomePage = new PassengerHomePage(driver);
        passengerHomePage.setRideRoutes();
        passengerHomePage.schedule();
        assert passengerHomePage.noAvailableDrivers();
        passengerHomePage.closeDialog();

        driver.quit();
    }


    @Test
    public void scheduleRide5_SchedulingAtALaterTimeShouldBeSuccessful() {
        String email = "later.time.schedule@email.com";
        String password = "Jovan123";
        String passengerHomePageUrl = "http://localhost:4200/passenger-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(passengerHomePageUrl));

        PassengerHomePage passengerHomePage = new PassengerHomePage(driver);
        passengerHomePage.setRideRoutes();
        passengerHomePage.setLaterTimeOnTimePicker();
        passengerHomePage.schedule();
        assert passengerHomePage.successfulRideSchedule();
        passengerHomePage.closeDialog();

        driver.quit();
    }

    @Test
    public void scheduleRide6_SettingRouteClearingRouteSettingRouteAgainScheduling() {
        String email = "clearing.routes.schedule@email.com";
        String password = "Jovan123";
        String passengerHomePageUrl = "http://localhost:4200/passenger-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(passengerHomePageUrl));

        PassengerHomePage passengerHomePage = new PassengerHomePage(driver);
        passengerHomePage.setRideRoutes();
        // TODO: Set time here
        passengerHomePage.clearRoutes();
        passengerHomePage.setRideRoutes();
        passengerHomePage.schedule();
        assert passengerHomePage.successfulRideSchedule();
        passengerHomePage.closeDialog();

        driver.quit();
    }

    @Test
    public void scheduleRide7_SchedulingAtPastTimeShouldNotSchedule() {
        String email = "past.time.schedule@email.com";
        String password = "Jovan123";
        String passengerHomePageUrl = "http://localhost:4200/passenger-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(passengerHomePageUrl));

        PassengerHomePage passengerHomePage = new PassengerHomePage(driver);
        passengerHomePage.setRideRoutes();
        passengerHomePage.setPastTimeOnTimePicker();
        passengerHomePage.schedule();
        assert passengerHomePage.tryingToScheduleRideInPast();
        passengerHomePage.closeDialog();

        driver.quit();
    }

    @Test
    public void scheduleRide8_SchedulingWithPassengersInvited() {
        String email = "passenger.inviting.schedule@email.com";
        String password = "Jovan123";
        String passengerHomePageUrl = "http://localhost:4200/passenger-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(passengerHomePageUrl));

        PassengerHomePage passengerHomePage = new PassengerHomePage(driver);
        passengerHomePage.setRideRoutes();
        // TODO: Set time here
        passengerHomePage.invitePassenger();
        passengerHomePage.schedule();
        assert passengerHomePage.successfulRideSchedule();
        passengerHomePage.closeDialog();

        driver.quit();
    }

    @Test
    public void scheduleRide9_SchedulingWithPassengerInvitedThenRemovePassenger() {
        String email = "remove.guest.schedule@email.com";
        String password = "Jovan123";
        String passengerHomePageUrl = "http://localhost:4200/passenger-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(passengerHomePageUrl));

        PassengerHomePage passengerHomePage = new PassengerHomePage(driver);
        passengerHomePage.setRideRoutes();
        // TODO: Set time here
        passengerHomePage.invitePassenger();
        passengerHomePage.removeInvitedPassenger();
        passengerHomePage.schedule();
        assert passengerHomePage.successfulRideSchedule();
        passengerHomePage.closeDialog();

        driver.quit();
    }

    @Test
    public void scheduleRide10_SchedulingWhileSettingAsFavorite() {
        String email = "set.favorite.schedule@email.com";
        String password = "Jovan123";
        String passengerHomePageUrl = "http://localhost:4200/passenger-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(passengerHomePageUrl));

        PassengerHomePage passengerHomePage = new PassengerHomePage(driver);
        passengerHomePage.setRideRoutes();
        // TODO: Set time here
        passengerHomePage.markAsFavorite();
        passengerHomePage.setFavoriteLocationName();
        passengerHomePage.schedule();
        assert passengerHomePage.successfullyAddedRideToFavorites();
        passengerHomePage.closeDialog2();
        assert passengerHomePage.successfulRideSchedule();
        passengerHomePage.closeDialog();

        driver.quit();
    }

    @Test
    public void scheduleRide11_SchedulingWhileSettingAsFavoriteButLeavingNameEmpty() {
        String email = "set.favorite.schedule@email.com";
        String password = "Jovan123";
        String passengerHomePageUrl = "http://localhost:4200/passenger-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(passengerHomePageUrl));

        PassengerHomePage passengerHomePage = new PassengerHomePage(driver);
        passengerHomePage.setRideRoutes();
        passengerHomePage.markAsFavorite();
        passengerHomePage.schedule();
        assert passengerHomePage.favoriteLocationNameMustBeProvided();
        passengerHomePage.closeDialog();

        driver.quit();
    }

    @Test
    public void scheduleRide12_UnsuccessfulScheduleWhenDriversWhoHaveThatVehicleTypeAreUnavailable() {
        String email = "luxury.vehicle.type.schedule@email.com";
        String password = "Jovan123";
        String passengerHomePageUrl = "http://localhost:4200/passenger-home";

        UnregisteredUserPage unregisteredUserPage = new UnregisteredUserPage(driver);
        unregisteredUserPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        wait.until(ExpectedConditions.urlToBe(passengerHomePageUrl));

        PassengerHomePage passengerHomePage = new PassengerHomePage(driver);
        passengerHomePage.setRideRoutes();
        passengerHomePage.setVehicleTypeAsLuxury();
        passengerHomePage.schedule();
        assert passengerHomePage.noAvailableDrivers();
        passengerHomePage.closeDialog();

        driver.quit();
    }

    @Test
    public void finishRide_happyCase() throws IOException {
        String driverEmail = customTestData.getDriver2().getEmailAddress();
        String driverPassword = "Mika1234";
        String passengerEmail = customTestData.getPassenger2().getEmailAddress();
        String passengerPassword = "Jovan123";
        String driverHomePageUrl = "http://localhost:4200/driver-home";
        String passengerHomePageUrl = "http://localhost:4200/passenger-home";

        WebDriver passengerDriver = new ChromeDriver();
        passengerDriver.manage().window().maximize();

        passengerDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        UnregisteredUserPage unregisteredUserPageForDriver = new UnregisteredUserPage(driver);
        unregisteredUserPageForDriver.login(driverEmail, driverPassword);

        UnregisteredUserPage unregisteredUserPageForPassenger = new UnregisteredUserPage(passengerDriver);
        unregisteredUserPageForPassenger.login(passengerEmail, passengerPassword);

        WebDriverWait waitDriver = new WebDriverWait(driver, Duration.ofSeconds(3L));
        WebDriverWait waitPassenger = new WebDriverWait(passengerDriver, Duration.ofSeconds(3L));

        waitDriver.until(ExpectedConditions.urlToBe(driverHomePageUrl));
        waitPassenger.until(ExpectedConditions.urlToBe(passengerHomePageUrl));

        DriverActiveRidePage driverActiveRidePage = new DriverActiveRidePage(driver);
        PassengerActiveRidePage passengerActiveRidePage = new PassengerActiveRidePage(passengerDriver);

        driverActiveRidePage.finishRide();
        waitDriver.until(ExpectedConditions.urlToBe(driverHomePageUrl));

        Assertions.assertTrue(passengerActiveRidePage.checkIfFinished());
        Assertions.assertEquals(driverHomePageUrl, driver.getCurrentUrl());
        driver.quit();
    }

}
