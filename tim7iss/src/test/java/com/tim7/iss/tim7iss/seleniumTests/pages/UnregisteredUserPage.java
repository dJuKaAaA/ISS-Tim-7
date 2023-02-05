package com.tim7.iss.tim7iss.seleniumTests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class UnregisteredUserPage {

    private static final String PAGE_URL = "http://localhost:4200/";
    private final WebDriver driver;

    @FindBy(xpath = "//p[text()='Login']")
    WebElement initialLoginButton;

    @FindBy(xpath = "//input[@name='login-email']")
    WebElement emailInput;

    @FindBy(xpath = "//input[@name='login-password']")
    WebElement passwordInput;

    @FindBy(xpath = "/html/body/app-root/app-unregistered-home/app-login/form/button")
    WebElement loginButton;

    @FindBy(xpath = "//mat-error")
    WebElement errorMessage;

    public UnregisteredUserPage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);

        PageFactory.initElements(driver, this);
    }

    public void login(String email, String password) {
        initialLoginButton.click();
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        loginButton.click();
    }

    public String getErrorMessage(){
       return errorMessage.getText();
    }

    public boolean isEmailValid(){
        String classValue = emailInput.getAttribute("class");
        return !classValue.contains("ng-invalid");
    }

    public boolean isPasswordValid() {
        String classValue = passwordInput.getAttribute("class");
        return !classValue.contains("ng-invalid");
    }

}
