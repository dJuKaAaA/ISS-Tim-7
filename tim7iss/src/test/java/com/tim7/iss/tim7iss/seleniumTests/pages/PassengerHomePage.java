package com.tim7.iss.tim7iss.seleniumTests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PassengerHomePage {

    private final String PAGE_URL = "http://localhost:4200/passenger-home";
    private final String DEPARTURE = "Katolicka Porta 4, Novi Sad";
    private final String DESTINATION = "Dunavski Park, Novi Sad";
    private final String PASSENGER_TO_INVITE = "guest.passenger.schedule@email.com";
    private final String FAVORITE_LOCATION_NAME = "Apartment to  University";

    private final WebDriver driver;
    private WebDriverWait wait;


    @FindBy(css = "#mat-input-0")
    private WebElement departureInput;

    @FindBy(css = "#mat-input-1")
    private WebElement destinationInput;

    @FindBy(css = "button.mdc-button:nth-child(3)")
    private WebElement showRouteButton;

    @FindBy(css = "#route-information > div.clear-map-buttons > button:nth-child(2)")
    private WebElement clearAllRoutesButton;

    @FindBy(css = "#mat-input-2")
    private WebElement rideTimePicker;

    @FindBy(css = "#mat-input-3")
    private WebElement invitePassengerInput;

    @FindBy(xpath = "//*[@id=\"route-information\"]/form[2]/mat-form-field/div[1]/div[2]/div[2]/button")
    private WebElement invitePassengerButton;

    @FindBy(xpath = "/html/body/app-root/app-passenger-home/app-schedule-ride/div/div[2]/form[2]/div/p[2]")
    private WebElement invitedPassengerContainer;

    @FindBy(css = "#mat-mdc-checkbox-1-input")
    private WebElement markAsFavoriteCheckbox;

    @FindBy(css = "#mat-input-4")
    private WebElement favoriteLocationNameInput;

    @FindBy(css = "#schedule-button")
    private WebElement scheduleButton;

    @FindBy(xpath = "/html/body/ngx-material-timepicker-container/div[2]/ngx-material-timepicker-content/div/div/div[2]/div[2]/ngx-material-timepicker-button/button")
    private WebElement timePickerOkButton;

    @FindBy(xpath = "/html/body/div[2]/div[2]/div/mat-dialog-container/div/div/app-dialog/div[2]/button")
    private WebElement closeDialogButton;

    @FindBy(xpath = "/html/body/ngx-material-timepicker-container/div[2]/ngx-material-timepicker-content/div/div/div[1]/div/ngx-material-timepicker-24-hours-face/ngx-material-timepicker-face/div/div/div[1]/span")
    private WebElement timePickerSomePastTimeButton;

    @FindBy(xpath = "/html/body/ngx-material-timepicker-container/div[2]/ngx-material-timepicker-content/div/div/div[1]/div/ngx-material-timepicker-24-hours-face/ngx-material-timepicker-face/div/div/div[13]/div[11]/span")
    private WebElement timePickerSomeLaterTimeButton;

    @FindBy(xpath = "/html/body/div[2]/div[2]/div/mat-dialog-container/div/div/app-dialog/div[1]")
    private WebElement dialogMessageDiv;

    @FindBy(xpath = "/html/body/div[2]/div[5]/div/mat-dialog-container/div/div/app-dialog/div[1]")
    private WebElement dialogMessageDiv2;  // dialog that will open at the same time as the main dialog to let the passenger know the ride was added to favorites

    @FindBy(xpath = "/html/body/div[2]/div[5]/div/mat-dialog-container/div/div/app-dialog/div[2]/button")
    private WebElement closeDialogButton2;  // close button for the dialog that will open at the same time as the main dialog to let the passenger know the ride was added to favorites

    public PassengerHomePage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);

        PageFactory.initElements(driver, this);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
    }

    public void setRideRoutes() {
        departureInput.click();
        departureInput.sendKeys(DEPARTURE);
        destinationInput.click();
        destinationInput.sendKeys(DESTINATION);
        showRouteButton.click();
        wait.until(ExpectedConditions.textToBePresentInElementValue(departureInput, DESTINATION));
        wait.until(ExpectedConditions.textToBePresentInElementValue(destinationInput, ""));
    }

    public void clearRoutes() {
        clearAllRoutesButton.click();
        wait.until(ExpectedConditions.textToBePresentInElementValue(departureInput, ""));
        wait.until(ExpectedConditions.textToBePresentInElementValue(destinationInput, ""));
    }

    public void invitePassenger() {
        invitePassengerInput.click();
        invitePassengerInput.sendKeys(PASSENGER_TO_INVITE);
        invitePassengerButton.click();
        wait.until(ExpectedConditions.visibilityOf(invitedPassengerContainer));
    }

    public void schedule() {
        scheduleButton.click();
    }

    public void closeDialog() {
        wait.until(ExpectedConditions.visibilityOf(closeDialogButton));
        closeDialogButton.click();
    }

    public boolean successfulRideSchedule() {
        wait.until(ExpectedConditions.visibilityOf(dialogMessageDiv));
        return dialogMessageDiv.getText().equals("Ride successfully scheduled");
    }

    public boolean notSpecifiedAnyRoutes() {
        wait.until(ExpectedConditions.visibilityOf(dialogMessageDiv));
        return dialogMessageDiv.getText().equals("You must specify at least one route of the ride");
    }

    public boolean tryingToScheduleRideInPast() {
        wait.until(ExpectedConditions.visibilityOf(dialogMessageDiv));
        return dialogMessageDiv.getText().equals("Ride can only be scheduled 5 minutes from now or later");
    }

    public void setPastTimeOnTimePicker() {
        rideTimePicker.click();
        timePickerSomePastTimeButton.click();
        timePickerOkButton.click();
    }

    public void setLaterTimeOnTimePicker() {
        rideTimePicker.click();
        timePickerSomeLaterTimeButton.click();
        timePickerOkButton.click();
    }

    public void removeInvitedPassenger() {
        wait.until(ExpectedConditions.visibilityOf(invitedPassengerContainer));
        invitedPassengerContainer.click();
    }

    public void markAsFavorite() {
        markAsFavoriteCheckbox.click();
    }

    public void setFavoriteLocationName() {
        wait.until(ExpectedConditions.visibilityOf(favoriteLocationNameInput));
        favoriteLocationNameInput.sendKeys(FAVORITE_LOCATION_NAME);
    }

    public boolean successfullyAddedRideToFavorites() {
        wait.until(ExpectedConditions.visibilityOf(dialogMessageDiv2));
        return dialogMessageDiv2.getText().equals("Successfully added to favorite locations");
    }

    public void closeDialog2() {
        wait.until(ExpectedConditions.visibilityOf(closeDialogButton2));
        closeDialogButton2.click();
    }

    public boolean favoriteLocationNameMustBeProvided() {
        wait.until(ExpectedConditions.visibilityOf(dialogMessageDiv));
        return dialogMessageDiv.getText().equals("Favorite location name must be provided");
    }

    public boolean passengerHasPendingRide() {
        wait.until(ExpectedConditions.visibilityOf(dialogMessageDiv));
        return dialogMessageDiv.getText().equals("Cannot create a ride while you have one already pending!");
    }

    public boolean noAvailableDrivers() {
        wait.until(ExpectedConditions.visibilityOf(dialogMessageDiv));
        return dialogMessageDiv.getText().equals("There are no available drivers at that moment");
    }

}
