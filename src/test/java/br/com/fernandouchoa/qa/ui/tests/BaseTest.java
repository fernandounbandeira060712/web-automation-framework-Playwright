package br.com.fernandouchoa.qa.ui.tests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.Video;

import br.com.fernandouchoa.qa.core.driver.DriverManager;
import br.com.fernandouchoa.qa.core.extensions.AllureReportExtension;
import br.com.fernandouchoa.qa.core.factory.PlaywrightFactory;
import br.com.fernandouchoa.qa.ui.pages.AccountPage;
import br.com.fernandouchoa.qa.ui.pages.CartPage;
import br.com.fernandouchoa.qa.ui.pages.CheckoutPage;
import br.com.fernandouchoa.qa.ui.pages.HomePage;
import br.com.fernandouchoa.qa.ui.pages.LoginPage;
import br.com.fernandouchoa.qa.ui.pages.ProductsPage;
import br.com.fernandouchoa.qa.utils.AllureUtils;

@ExtendWith(AllureReportExtension.class)
public abstract class BaseTest {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BaseTest.class);

    protected Page page;

    protected HomePage homePage;
    protected LoginPage loginPage;
    protected ProductsPage productsPage;
    protected CartPage cartPage;
    protected CheckoutPage checkoutPage;
    protected AccountPage accountPage;

    @BeforeEach
    public void setup() {
        PlaywrightFactory.createInstance();
        page = PlaywrightFactory.getPage();

        homePage = new HomePage(page);
        homePage.open();
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {
        Video video = getVideoSafely();
        Path tracePath = buildTracePath(testInfo);

        stopTraceAndAttach(tracePath);

        try {
            PlaywrightFactory.closeInstance();
        } finally {
            attachVideo(video);
            clearPageObjects();
        }
    }

    private Path buildTracePath(TestInfo testInfo) {
        String testName = testInfo.getDisplayName()
                .replace("()", "")
                .replaceAll("[^a-zA-Z0-9-_]", "_");

        return Paths.get("target", "traces", testName + ".zip");
    }

    private Video getVideoSafely() {
        try {
            return page != null && !page.isClosed()
                    ? page.video()
                    : null;
        } catch (RuntimeException exception) {
            LOGGER.warn("Não foi possível obter o vídeo da execução.", exception);
            return null;
        }
    }

    private void stopTraceAndAttach(Path tracePath) {
        BrowserContext context = DriverManager.getContext();

        if (context == null) {
            return;
        }

        try {
            Files.createDirectories(tracePath.getParent());

            context.tracing().stop(
                    new Tracing.StopOptions().setPath(tracePath)
            );

            AllureUtils.attachFile(
                    "Trace Playwright",
                    tracePath,
                    "application/zip",
                    ".zip"
            );
        } catch (Exception exception) {
            LOGGER.warn("Não foi possível salvar o trace do Playwright.", exception);
        }
    }

    private void attachVideo(Video video) {
        if (video == null) {
            return;
        }

        try {
            AllureUtils.attachFile(
                    "Vídeo da execução",
                    video.path(),
                    "video/webm",
                    ".webm"
            );
        } catch (Exception exception) {
            LOGGER.warn("Não foi possível anexar o vídeo da execução.", exception);
        }
    }

    private void clearPageObjects() {
        page = null;
        homePage = null;
        loginPage = null;
        productsPage = null;
        cartPage = null;
        checkoutPage = null;
        accountPage = null;
    }
}
