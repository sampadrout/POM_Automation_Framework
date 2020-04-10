/*********************************************************************************************************
 * File Name: StartGenymotion.java
 * Author: Sampad Rout
 * Creation Date: 24-Feb-2016
 * Description: This is the class created to start Genymotion emulator
 * Copyright:
 *********************************************************************************************************/

package org.framework.utils;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;

public class StartGenymotion {

    private static String deviceName = "Google_Nexus_6P_7.1";

    public static void startEmulator() throws ExecuteException, IOException, InterruptedException {

        DefaultExecutor executor = new DefaultExecutor();
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        CommandLine launchEmul = new CommandLine("open");
        launchEmul.addArgument("-a");
        launchEmul.addArgument("/Applications/Genymotion.app/Contents/MacOS/player.app");
        launchEmul.addArgument("--args");
        launchEmul.addArgument("--vm-name");
        launchEmul.addArgument("\""+deviceName+"\"");
        executor.setExitValue(1);
        executor.execute(launchEmul, resultHandler);
        System.out.println("Genymotion emulator started");
    }
}
