package com.percy;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.percy.selenium.Percy;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

class PercyAfterTest {

    public Percy percy ;
    public WebDriverWait webDriverWait;

    public WebDriver driver;

    public String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    public String AUTOMATE_KEY =  System.getenv("BROWSERSTACK_ACCESS_KEY");
    public String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    @BeforeMethod(alwaysRun = true)
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {

        MutableCapabilities capabilities = new MutableCapabilities();
        HashMap<String, Object> bstackOptions = new HashMap<String, Object>();
        capabilities.setCapability("browserName", "Chrome");
        bstackOptions.put("os", "Windows");
        bstackOptions.put("osVersion", "10");
        bstackOptions.put("browserVersion", "120.0");
        bstackOptions.put("consoleLogs", "info");
        bstackOptions.put("seleniumVersion", "4.20.0");
        bstackOptions.put("projectName", "My Project");
        bstackOptions.put("buildName", "test percy_screenshot");
        bstackOptions.put("sessionName", "Percy second_test");
        capabilities.setCapability("bstack:options", bstackOptions);
        driver = new RemoteWebDriver(new URL(URL), capabilities);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }


    @Test
    public void addProductToCart() throws Exception {

        try{
            //webdriver initilization
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            //percy initilization
            percy = new Percy(driver);

            driver.manage().window().setSize(new Dimension(1280, 1024));
            // navigate to bstackdemo
            driver.get("https://www.bstackdemo.com");

            // Check the title
            webDriverWait.until(ExpectedConditions.titleContains("StackDemo"));

            // click on the samsung product
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__next']/div/div/main/div[1]/div[2]/label/span")));
            driver.findElement(By.xpath("//*[@id='__next']/div/div/main/div[1]/div[2]/label/span")).click();

            // [percy note: important step]
            // Percy Screenshot 1
            // take percy_screenshot using the following command
            percy.screenshot("screenshot_1");

            // Save the text of the product for later verify
            String productOnScreenText = driver.findElement(By.xpath("//*[@id=\"10\"]/p")).getText();

            // Click on add to cart button
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"10\"]/div[4]")));
            driver.findElement(By.xpath("//*[@id=\"10\"]/div[4]")).click();

            // See if the cart is opened or not
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("float-cart__content")));

            // Get text of product in cart
            String productOnCartText = driver.findElement(By.xpath("//*[@id=\"__next\"]/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]")).getText();

            // [percy note: important step]
            // Percy Screenshot 2
            // take percy_screenshot using the following command
            percy.screenshot("screenshot_2");

            Assert.assertEquals(productOnScreenText, productOnCartText);


        } catch (Exception e) {
            System.out.println("Error occured while executing script :" + e);
        }
    }
}
