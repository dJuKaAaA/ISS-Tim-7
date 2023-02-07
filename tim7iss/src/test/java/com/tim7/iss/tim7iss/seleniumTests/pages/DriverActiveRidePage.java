package com.tim7.iss.tim7iss.seleniumTests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DriverActiveRidePage {
    private static final String PAGE_URL = "http://localhost:4200/driver-current-ride/11";
    private final WebDriver driver;

    @FindBy(xpath = "//*[@id=\"button-layout\"]/button[1]")
    WebElement finishRideButton;

    public DriverActiveRidePage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);
        PageFactory.initElements(driver, this);
    }

    public void finishRide(){
        finishRideButton.click();
    }
}
