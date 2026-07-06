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
import com.microsoft.playwright.Video;

import br.com.fernandouchoa.qa.core.driver.DriverManager;
import br.com.fernandouchoa.qa.core.extensions.AllureReportExtension;
import br.com.fernandouchoa.qa.core.factory.PlaywrightFactory;
import br.com.fernandouchoa.qa.core.retry.RetryTestExtension;
import br.com.fernandouchoa.qa.ui.pages.AccountPage;
import br.com.fernandouchoa.qa.ui.pages.CartPage;
import br.com.fernandouchoa.qa.ui.pages.CheckoutPage;
import br.com.fernandouchoa.qa.ui.pages.HomePage;
import br.com.fernandouchoa.qa.ui.pages.LoginPage;
import br.com.fernandouchoa.qa.ui.pages.ProductsPage;
import br.com.fernandouchoa.qa.utils.AllureUtils;

@ExtendWith({
        AllureReportExtension.class,
        RetryTestExtension.class
})
public class BaseTest {

    protected Page page;

    protected HomePage homePage;
    protected LoginPage loginPage;
    protected ProductsPage productsPage;
    protected CartPage cartPage;
    protected CheckoutPage checkoutPage;
    protected AccountPage accountPage;

    private static boolean cleaned = false;

    @BeforeEach
    public void setup() {

        if (!cleaned) {
            AllureUtils.cleanResults();
            cleaned = true;
        }

        PlaywrightFactory.createInstance();

        page = PlaywrightFactory.getPage();

        homePage = new HomePage(page);
        homePage.open();
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {

        String testName =
                testInfo.getDisplayName()
                        .replace("()", "")
                        .replaceAll("[^a-zA-Z0-9-_]", "_");

        Path tracePath =
                Paths.get("target", "traces", testName + ".zip");

        Video video =
                page != null ? page.video() : null;

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

                AllureUtils.attachFile(
                        "Trace Playwright",
                        tracePath,
                        "application/zip"
                );
            }
        } catch (Exception ignored) {
        }

        PlaywrightFactory.closeInstance();

        try {
            if (video != null) {
                AllureUtils.attachFile(
                        "Vídeo da execução",
                        video.path(),
                        "video/webm",
                        ".webm"
                );
            }
        } catch (Exception ignored) {
        }
    }
}