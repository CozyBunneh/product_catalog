# Product frontend

## Install dependencies

### Arch Linux

```sh
yay -S docker docker-compose docker-credential-pass nodejs npm chromium
```

## Basic setup of a project like this

```sh
npx create-react-app test-app2 --template=typescript
```

Eslint & Prettier
```sh
npm init @eslint/config
```

And the install puppeteer:
```sh
npm install puppeteer puppeteer-core jest-puppeteer
```

Then all of the configuration and such is needed

## Run

```sh
npm start
```

## Docker

```sh
docker build -t product-react-app .
docker run -d -p 80:80 product-react-app
```

Open:
```sh
http://localhost
```

## E2E tests

Chromium needs to be installed due to puppeteer being used.
```sh
yay -S chromium
```

Get the path to it:
```sh
whereis chromium
```
And set it to that path in the e2e test files for `executablePath`.

### Run E2E tests

You may need separate terminals
```sh
npm start
npm run test:e2e
```
