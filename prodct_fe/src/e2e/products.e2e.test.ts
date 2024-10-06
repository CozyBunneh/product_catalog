import puppeteer, { Browser, executablePath, Page } from "puppeteer-core";
import "core-js";
import { TextEncoder, TextDecoder } from 'util'
global.TextEncoder = TextEncoder
// @ts-expect-error
global.TextDecoder = TextDecoder


describe("Products Tests", () => {
  let browser: Browser;
  let page: Page;

  const baseUrl = "http://localhost:3000";  // Adjust if running on a different port

  beforeAll(async () => {
    browser = await puppeteer.launch({
      headless: true,
      devtools: true,
      args: [
        '--disable-web-security',
        '--disable-features=IsolateOrigins',
        '--disable-site-isolation-trials'
      ],
      executablePath: "/usr/bin/chromium",
    });
    page = await browser.newPage();
    await page.goto(baseUrl);  // Navigate to the app before running any tests
  });

  afterAll(async () => {
    await browser.close();
  })
});
