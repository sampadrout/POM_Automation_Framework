package tests;

import org.testng.annotations.*;

import pageobject.HomeScreenPO;

import org.framework.allureReport.TestListener;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

@Listeners({TestListener.class})
public class TestCases extends BaseTest{

    @Test(description = "View login page")
    @Severity(SeverityLevel.CRITICAL)
    public void test() {
        HomeScreenPO homeScreenPO = new HomeScreenPO(driver);
        homeScreenPO.tapOnLoginScreenTextView();
    }
}
