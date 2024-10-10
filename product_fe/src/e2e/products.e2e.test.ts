import puppeteer, { Browser, Page } from "puppeteer-core";
import "core-js";
import { TextEncoder, TextDecoder } from 'util'
import { ProductV1 } from "../features/product/type/productTypes";
global.TextEncoder = TextEncoder
// @ts-expect-error
global.TextDecoder = TextDecoder


describe("Products Tests", () => {
  let browser: Browser;
  let page: Page;

  const baseUrl = "http://localhost:3000";

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

  it("should load the products from API", async () => {
    // Mock API response for product loading using page.evaluate to stub fetch
    await page.setRequestInterception(true);
    page.on("request", (req) => {
      if (req.url().endsWith("products")) {
        req.respond({
          status: 200,
          contentType: "application/json",
          body: JSON.stringify([
            {
              id: "3a3bf02f-5152-407e-83a6-fdbdb4876931",
              name: "name1",
              category: "category1",
              description: "description1",
              price: 1.0,
              imageUrl: "imageUrl1"
            } as ProductV1,
            {
              id: "838412d6-6155-41d3-abef-364859167140",
              name: "name2",
              category: "category2",
              description: "description2",
              price: 2.0,
              imageUrl: "imageUrl2"
            } as ProductV1,
          ]),
        });
      } else {
        req.continue();
      }
    });

    await page.reload();  // Reload the page to trigger the API request

    const productItems = await page.$$("tbody tr");
    expect(productItems.length).toBe(2);
    const firstProductName = await page.evaluate(el => el.cells[0].textContent, productItems[0]);
    const firstProductCategory = await page.evaluate(el => el.cells[1].textContent, productItems[0]);
    const firstProductPrice = await page.evaluate(el => el.cells[2].innerText, productItems[0]);
    const secondProductName = await page.evaluate(el => el.cells[0].textContent, productItems[1]);
    const secondProductCategory = await page.evaluate(el => el.cells[1].textContent, productItems[1]);
    const secondProductPrice = await page.evaluate(el => el.cells[2].innerText, productItems[1]);
    expect(firstProductName).toContain("name1");
    expect(firstProductCategory).toContain("category1");
    expect(firstProductPrice).toContain("€1");
    expect(secondProductName).toContain("name2");
    expect(secondProductCategory).toContain("category2");
    expect(secondProductPrice).toContain("€2");

  });
});
