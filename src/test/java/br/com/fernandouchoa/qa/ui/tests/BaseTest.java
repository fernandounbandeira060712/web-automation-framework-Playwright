package br.com.fernandouchoa.qa.ui.tests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;

import br.com.fernandouchoa.qa.core.driver.DriverManager;
import br.com.fernandouchoa.qa.core.extensions.AllureReportExtension;
import br.com.fernandouchoa.qa.core.factory.PlaywrightFactory;
import br.com.fernandouchoa.qa.utils.AllureUtils;

@ExtendWith(AllureReportExtension.class)
public class BaseTest {

    protected Page page;
    private static boolean cleaned = false;

    @BeforeEach
    public void setup() {

        if (!cleaned) {
            AllureUtils.cleanResults();
            cleaned = true;
        }

        PlaywrightFactory.createInstance();
        page = PlaywrightFactory.getPage();
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {

        String testName =
                testInfo.getDisplayName()
                        .replace("()", "")
                        .replaceAll("[^a-zA-Z0-9-_]", "_");

        Path tracePath =
                Paths.get("target", "traces", testName + ".zip");

        if (page != null) {
            AllureUtils.attachScreenshot(
                    page,
                    "Captura de tela - " + testInfo.getDisplayName()
            );
        }

        try {
            if (DriverManager.getContext() != null) {

                Files.createDirectories(tracePath.getParent());

                DriverManager.getContext()
                        .tracing()
                        .stop(
                                new Tracing.StopOptions()
                                        .setPath(tracePath)
                        );
                
                System.out.println("TRACE: " + tracePath.toAbsolutePath());
                System.out.println("EXISTS: " + Files.exists(tracePath));
                System.out.println("SIZE: " + Files.size(tracePath));

                AllureUtils.attachFile(
                        "Trace Playwright",
                        tracePath,
                        "application/zip"
                );
            }
        } catch (Exception ignored) {
        }

        PlaywrightFactory.closeInstance();
    }
}