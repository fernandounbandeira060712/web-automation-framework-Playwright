package br.com.fernandouchoa.qa.core.factory;

import java.nio.file.Paths;
import java.util.Locale;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;

import br.com.fernandouchoa.qa.core.config.EnvironmentManager;
import br.com.fernandouchoa.qa.core.driver.DriverManager;

public final class PlaywrightFactory {

    private PlaywrightFactory() {
    }

    public static void createInstance() {
        Playwright playwright = Playwright.create();

        BrowserType.LaunchOptions options =
                new BrowserType.LaunchOptions()
                        .setHeadless(EnvironmentManager.isHeadless());

        Browser browser =
                launchBrowser(playwright, options);

        BrowserContext context =
                browser.newContext(
                        new Browser.NewContextOptions()
                                .setRecordVideoDir(
                                        Paths.get("target", "videos")
                                )
                                .setRecordVideoSize(1280, 720)
                );

        context.setDefaultTimeout(
                EnvironmentManager.getTimeout()
        );

        context.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );

        Page page = context.newPage();

        DriverManager.setPlaywright(playwright);
        DriverManager.setBrowser(browser);
        DriverManager.setContext(context);
        DriverManager.setPage(page);
    }

    public static Page getPage() {
        return DriverManager.getPage();
    }

    public static void closeInstance() {
        if (DriverManager.getPage() != null) {
            DriverManager.getPage().close();
        }

        if (DriverManager.getContext() != null) {
            DriverManager.getContext().close();
        }

        if (DriverManager.getBrowser() != null) {
            DriverManager.getBrowser().close();
        }

        if (DriverManager.getPlaywright() != null) {
            DriverManager.getPlaywright().close();
        }

        DriverManager.clear();
    }

    private static Browser launchBrowser(
            Playwright playwright,
            BrowserType.LaunchOptions options) {

        String browserName =
                EnvironmentManager.getBrowser();

        switch (browserName.toLowerCase(Locale.ROOT)) {
            case "firefox":
                return playwright.firefox().launch(options);

            case "webkit":
                return playwright.webkit().launch(options);

            case "chromium":
                return playwright.chromium().launch(options);

            default:
                throw new IllegalArgumentException(
                        "Browser não suportado: " + browserName
                );
        }
    }
}