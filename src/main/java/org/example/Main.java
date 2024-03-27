package org.example;

import com.microsoft.playwright.*;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            BrowserType browserType = playwright.chromium();
            Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            page.navigate("https://playwright.dev");
            System.out.println(page.title());

            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}