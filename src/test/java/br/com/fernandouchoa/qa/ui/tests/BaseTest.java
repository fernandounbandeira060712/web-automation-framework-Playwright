package br.com.fernandouchoa.qa.ui.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import com.microsoft.playwright.Page;

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

        if (page != null) {
            AllureUtils.attachScreenshot(
                    page,
                    "Captura de tela - " + testInfo.getDisplayName()
            );
        }

        PlaywrightFactory.closeInstance();
    }
}