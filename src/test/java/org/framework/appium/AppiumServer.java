package org.framework.appium;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;
import java.io.IOException;

public class AppiumServer {

    public static AppiumDriverLocalService appium;

    public static void start() throws IOException {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "debug") // error:info, debug
                .withLogFile(new File(System.getProperty("user.dir") + "/appiumlogs/appiumLogs.txt"));
        appium = builder.build();
        appium.start();
        System.out.println("Appium server has been started");
    }

    public static void stop() throws IOException {
        appium.stop();
        System.out.println("Appium server has been stopped");
    }
}