package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import org.junit.jupiter.api.*;

import java.util.regex.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.*;

public class TestRunner {

    //mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen https://playwright.dev/"

    @Tag("windows")
    @Test
    public void navigation_TestCase() {

        try (Playwright playwright = Playwright.create()) {
            BrowserType browserType = playwright.chromium();
            Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();

            Page page = context.newPage();
            page.navigate("https://playwright.dev/");

            assertThat(page).hasTitle(Pattern.compile("Fast and reliable end-to-end testing for modern web apps | Playwright Java"));

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get started")).click();
            assertThat(page).hasTitle(Pattern.compile("Installation | Playwright")); //
            assertThat(page.getByLabel("Home page")).isVisible();

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Installation")).click();
            assertThat(page).hasTitle(Pattern.compile("Installation | Playwright")); //
            assertThat(page.getByLabel("Home page")).isVisible();

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Writing tests").setExact(true)).click();
            assertThat(page).hasTitle(Pattern.compile("Writing tests | Playwright")); //
            assertThat(page.getByLabel("Home page")).isVisible();

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Generating tests").setExact(true)).click();
            assertThat(page).hasTitle(Pattern.compile("Writing tests | Playwright")); //
            assertThat(page.getByLabel("Home page")).isVisible();

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Running and debugging tests").setExact(true)).click();
            assertThat(page.locator("h1")).containsText("Running and debugging tests");
            assertThat(page.getByLabel("Home page")).isVisible();

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Release notes")).click();
            assertThat(page.locator("h1")).containsText("Release notes");
            assertThat(page.locator("#version-143")).containsText("Version 1.43");
            assertThat(page.locator("#new-apis")).containsText("New APIs");
            assertThat(page.getByLabel("Home page")).isVisible();

        }
    }

    @Tag("mobile")
    @Test
    public void alt_navigation_TestCase() {
        try (Playwright playwright = Playwright.create()) {
            BrowserType browserType = playwright.chromium();
            Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();

            Page page = context.newPage();
            page.navigate("https://playwright.dev/");

            assertThat(page).hasTitle(Pattern.compile("Fast and reliable end-to-end testing for modern web apps | Playwright Java"));

            Locator link = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get started"));
            link.click();

            assertThat(page).hasTitle(Pattern.compile("Installation | Playwright")); //

        }
    }
}