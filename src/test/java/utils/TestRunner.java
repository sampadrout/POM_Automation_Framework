package utils;

import utils.FileUtility;
import org.testng.TestNG;
import org.testng.collections.Lists;

import java.io.IOException;
import java.util.List;

import static utils.LoggingManager.logMessage;

public class TestRunner {
    public static void main(String[] args) throws IOException {
        TestNG testng = new TestNG();

        List<String> suites = Lists.newArrayList();
        suites.add(FileUtility.getFile("testng.xml").getAbsolutePath());
        testng.setTestSuites(suites);
        testng.run();
    }
}
