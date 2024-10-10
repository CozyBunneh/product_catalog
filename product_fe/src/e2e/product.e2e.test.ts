import puppeteer, { Browser, Page } from "puppeteer-core";
import "core-js";
import { TextEncoder, TextDecoder } from 'util'
import { ProductV1 } from "../features/product/type/productTypes";
global.TextEncoder = TextEncoder
// @ts-expect-error
global.TextDecoder = TextDecoder


describe("Product Tests", () => {
  let browser: Browser;
  let page: Page;

  const baseUrl = "http://localhost:3000/3a3bf02f-5152-407e-83a6-fdbdb4876931";

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
    await page.goto(baseUrl);
  });

  afterAll(async () => {
    await browser.close();
  })

  it("should load the product from API", async () => {
    // Mock API response for product loading using page.evaluate to stub fetch
    await page.setRequestInterception(true);
    page.on("request", (req) => {
      if (req.url().endsWith("products/3a3bf02f-5152-407e-83a6-fdbdb4876931")) {
        req.respond({
          status: 200,
          contentType: "application/json",
          body: JSON.stringify(
            {
              id: "3a3bf02f-5152-407e-83a6-fdbdb4876931",
              name: "name1",
              category: "category1",
              description: "description1",
              price: 1.0,
              imageUrl: "imageUrl1"
            } as ProductV1,
          ),
        });
      } else {
        req.continue();
      }
    });

    await page.reload();  // Reload the page to trigger the API request

    const productValues = await page.$$("p");
    expect(productValues.length).toBe(6);

    const id = await page.evaluate(el => el.textContent, productValues[0]);
    const name = await page.evaluate(el => el.textContent, productValues[1]);
    const category = await page.evaluate(el => el.textContent, productValues[2]);
    const description = await page.evaluate(el => el.textContent, productValues[3]);
    const price = await page.evaluate(el => el.textContent, productValues[4]);
    const imageUrl = await page.evaluate(el => el.textContent, productValues[5]);
    expect(id).toContain("3a3bf02f-5152-407e-83a6-fdbdb4876931");
    expect(name).toContain("name1");
    expect(category).toContain("category1");
    expect(description).toContain("description1");
    expect(price).toContain("€1");
    expect(imageUrl).toContain("imageUrl1");
  });
});
