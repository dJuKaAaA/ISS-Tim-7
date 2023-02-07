package com.tim7.iss.tim7iss.seleniumTests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PassengerActiveRidePage {
    private static final String PAGE_URL = "http://localhost:4200/passenger-current-ride/11";
    private final WebDriver driver;

    @FindBy(xpath = "//*[@id=\"mat-mdc-dialog-0\"]/div")
    WebElement ratingDialog;

    public PassengerActiveRidePage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);
        PageFactory.initElements(driver, this);
    }

    public boolean checkIfFinished(){
        return ratingDialog.isDisplayed();
    }
}
