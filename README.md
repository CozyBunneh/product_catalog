# product_catalog
Internal Retail Product Catalog with Fuzzy Search Capability

## [Frontend README](./product_fe/README.md)
## [Backend README](./product_api/README.md)

## Install dependencies

### Arch Linux

```sh
yay -S docker docker-compose docker-credential-pass nodejs npm chromium jdk-openjdk jdtls unzip spring-boot-cli jdk22-graalvm-ee-bin docker docker-compose docker-credential-pass
```

## Setup

### Docker

#### Arch Linux

https://wiki.archlinux.org/title/Docker#Rootless_Docker_daemon

```sh
sudo groupadd docker

sudo usermod -aG docker $USER

# You may have to reboot
```

