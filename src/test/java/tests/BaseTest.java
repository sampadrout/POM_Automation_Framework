package tests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.testng.ITestResult;
import org.testng.annotations.*;

import utils.AppiumServer;
import utils.PropertyUtils;
import utils.WaitUtils;
import static utils.LoggingManager.logMessage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

//@Listeners({ScreenshotUtility.class})
public abstract class BaseTest {

    /**
     * As driver static it will be created only once and used across all of the test classes.
     */
    public static AppiumDriver driver;
    public final static String APPIUM_SERVER_URL = PropertyUtils.getProperty("appium.server.url", "http://127.0.0.1:4723/wd/hub");
    public final static int IMPLICIT_WAIT = PropertyUtils.getIntegerProperty("implicitWait", 30);
    public static WaitUtils waitUtils = new WaitUtils();

    private void setDesiredCapabilitiesForAndroid(DesiredCapabilities desiredCapabilities) {
        String PLATFORM_NAME = PropertyUtils.getProperty("android.platform");
        String PLATFORM_VERSION = PropertyUtils.getProperty("android.platform.version");
        String APP_NAME = PropertyUtils.getProperty("android.app.name");
        String APP_RELATIVE_PATH = PropertyUtils.getProperty("android.app.location") + APP_NAME;
        String APP_PATH = getAbsolutePath(APP_RELATIVE_PATH);
        String DEVICE_NAME = PropertyUtils.getProperty("android.device.name");
        String APP_PACKAGE_NAME = PropertyUtils.getProperty("android.app.packageName");
        String APP_ACTIVITY_NAME = PropertyUtils.getProperty("android.app.activityName");
        String APP_FULL_RESET = PropertyUtils.getProperty("android.app.full.reset");
        String APP_NO_RESET = PropertyUtils.getProperty("android.app.no.reset");

        desiredCapabilities.setCapability("avd", "Pixel3");
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
//        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, DEVICE_NAME);
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, PLATFORM_VERSION);
        desiredCapabilities.setCapability(MobileCapabilityType.APP, APP_PATH); // Use APP_PATH if the file is in the local folder
        desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, APP_PACKAGE_NAME);
        desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, APP_ACTIVITY_NAME);
        desiredCapabilities.setCapability(MobileCapabilityType.FULL_RESET, APP_FULL_RESET);
        desiredCapabilities.setCapability(MobileCapabilityType.NO_RESET, APP_NO_RESET);
        desiredCapabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
    }

    // kill the node apps and start Appium Server
    @BeforeSuite
    public void startAppiumServer() throws IOException {
        logMessage("---Before suite---");

        killExistingAppiumProcess();
        killExistingAppiumProcess();

        // creating appium logs folder and files
        File appiumLogDir = new File(System.getProperty("user.dir") + "/appiumlogs");
        if (!appiumLogDir.exists()) {
            appiumLogDir.mkdir();
        }
        File logFile = new File(appiumLogDir, "appiumLogs.txt");
        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        if (AppiumServer.appium == null || !AppiumServer.appium.isRunning()) {
                AppiumServer.start();
                logMessage("Appium server has been started");
            }
    }

    @BeforeTest
    public void setUpPage() {
        logMessage("---Before test---");
    }

    @BeforeClass
    public void runBeforeClass() {
        logMessage("---Before class---");
    }

    // instantiate the ppium driver
    @BeforeMethod
    public void setUpAppium() throws MalformedURLException {
        logMessage("---Before Method---");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        setDesiredCapabilitiesForAndroid(capabilities);
        driver = new AppiumDriver(new URL(APPIUM_SERVER_URL), capabilities);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(final ITestResult result) throws IOException {
        logMessage("---After method---");

        String fileName = result.getTestClass().getName() + "_" + result.getName();
        logMessage("Test Case: [" + fileName + "] executed..!");
    }

    @AfterClass
    public void afterClass() {
        logMessage("---After class---");
    }

    @AfterSuite
    public void tearDownAppium() {
        logMessage("---After suite---");

        quitDriver();

//        try {
//            Process process = Runtime.getRuntime().exec("adb -s emulator-5554 emu kill");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void quitDriver() {
        try {
            this.driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WebDriver getScreenshotableWebDriver() {
        final WebDriver augmentedDriver = new Augmenter().augment(driver);
        return augmentedDriver;
    }

    private void killExistingAppiumProcess() throws IOException {
        Runtime.getRuntime().exec("killall node");
        logMessage("Killing existing appium process");
    }

    public void stopAppiumServer(String platformType, @Optional String platformName) throws IOException {
        if (AppiumServer.appium != null || AppiumServer.appium.isRunning()) {
            AppiumServer.stop();
            logMessage("Appium server has been stopped");
        }
    }

    private static void setTimeOuts(AppiumDriver driver) {
        //Use a higher value if your mobile elements take time to show up
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
    }

    private static String getAbsolutePath(String appRelativePath) {
        File file = new File(appRelativePath);
        return file.getAbsolutePath();
    }
}