package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.*;

public class TestRunner {

    //mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen https://playwright.dev/"
    //mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen https://www.amazon.co.uk/"
    static BrowserType browserType;
    static Browser browser;

    @BeforeAll
    static void setup() {
        String setbrowser = System.getProperty("browser");
        com.microsoft.playwright.Playwright playwright = com.microsoft.playwright.Playwright.create();

        switch (setbrowser) {
            case "chrome":
                browserType = playwright.chromium();
                browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(true));
                break;
            case "firefox":
                browserType = playwright.firefox();
                browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(true));
                break;
            case "safari":
                browserType = playwright.webkit();
                browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(true));
                break;
            case "webkit":
                browserType = playwright.webkit();
                browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(true));
                break;
            case "brave":
                browserType = playwright.chromium();
                Path braveExecutablePath = Path.of("/opt/brave.com/brave/brave-browser");
                browser = browserType.launch(new BrowserType.LaunchOptions().setExecutablePath(braveExecutablePath).setHeadless(true));
                break;
            default:
                System.out.println("Browser value " + browser + " does not match expectation");
        }

    }

    @Tag("windows")
    @Test
    public void navigation_TestCase() {


        try(Browser mbrowser = browser ) {
            BrowserContext context = mbrowser.newContext();
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

        try(Browser mbrowser = browser ) {
            BrowserContext context = mbrowser.newContext();
            Page page = context.newPage();

            page.navigate("https://www.bbc.co.uk/");

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes, I agree")).click();
            page.getByTestId("header-content").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("News")).click();

            assertThat(page.getByTestId("masthead")).containsText("BBC News");
            assertThat(page.locator("#product-navigation-menu")).containsText("Home");
            assertThat(page.getByTestId("more-menu-button").getByRole(AriaRole.BUTTON)).containsText("More");
            assertThat(page.getByTestId("sign-in-banner").getByRole(AriaRole.HEADING)).containsText("Discover your BBC");
            page.getByTestId("header-content").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Sport")).click();
            assertThat(page.getByTestId("masthead")).containsText("BBC Sport");
            assertThat(page.getByTestId("navigation").getByRole(AriaRole.LIST)).containsText("Home");
            assertThat(page.getByTestId("more-menu-button").getByRole(AriaRole.BUTTON)).containsText("More");
            assertThat(page.getByTestId("header-content").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Weather"))).isVisible();
            page.getByTestId("header-content").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Weather")).click();
            assertThat(page.locator("form")).containsText("Search");
            assertThat(page.getByPlaceholder("Enter a town, city or UK")).isVisible();
            assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Find my location"))).isVisible();
            page.getByLabel("BBC-wide").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("iPlayer")).click();
            assertThat(page.locator("#main")).containsText("iPlayer NavigationiPlayer Accessibility HelpMenu");
            page.getByLabel("BBC-wide").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Sounds")).click();
            assertThat(page.getByLabel("BBC Sounds", new Page.GetByLabelOptions().setExact(true))).containsText("BBC SoundsSounds home pageMenuHomeMusicPodcastsMy Sounds");
            page.getByLabel("BBC-wide").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Bitesize")).click();

            assertThat(page.getByTestId("masthead")).containsText("Bitesize!");

        }
    }

    @Tag("data")
    @ParameterizedTest
    @MethodSource("testData")
    public void testdata_TestCase(String product, String result) {
        // List to collect exceptions
        List<Throwable> exceptions = new ArrayList<>();

//        try(browser ) {

            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.navigate("https://www.amazon.co.uk/");

            page.getByLabel("Accept").click();
            page.getByPlaceholder("Search Amazon.co.uk").click();
            page.getByPlaceholder("Search Amazon.co.uk").fill(product);
            page.getByPlaceholder("Search Amazon.co.uk").press("Enter");
            page.getByLabel("navigation").getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Go")).click();

            customAssert(() -> assertThat(page.locator("#search")).containsText(result), exceptions);

            // Throw any collected exceptions after the test case is finished
            if (!exceptions.isEmpty()) {
                throw new AssertionError("One or more assertions failed:", exceptions.get(0));
            }


//        }

    }
    static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of("cleaner", "Results"),
                Arguments.of("mixer", "Results"),
                Arguments.of("dryer", "Results")
        );
    }

    private void customAssert(Executable executable, List<Throwable> exceptions) {
        try {
            executable.execute();
        } catch (Throwable t) {
            exceptions.add(t);
        }
    }

    interface Executable {
        void execute() throws Throwable;
    }
}