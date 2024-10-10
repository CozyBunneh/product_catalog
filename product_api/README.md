# Product backend api

## To Setup a similar project from the start

```sh
# install
yay -S jdk-openjdk jdtls unzip spring-boot-cli jdk22-graalvm-ee-bin docker docker-compose docker-credential-pass

# generate project
spring init \
  --artifact-id product_api \
  --build gradle \
  --type gradle-project \
  --group-id com.product \
  --java-version 22 \
  --language java \
  --packaging jar \
  --dependencies webflux,data-r2dbc,liquibase,devtools,validation
unzip product_api.zip -d product_api

# navigate into folder
mkdir product_api

# correct or comment out the generated unit test for now or it will not build
```

### Helix Editor

To better exclude unecessary files from helix file picker

file: .ignore
```
HELP.md
.gradle
build/
!gradle/wrapper/gradle-wrapper.jar
!**/src/main/**/build/
!**/src/test/**/build/

### STS ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache
bin/
!**/src/main/**/bin/
!**/src/test/**/bin/

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr
out/
!**/src/main/**/out/
!**/src/test/**/out/

### NetBeans ###
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/

### VS Code ###
.vscode/
```

## Build

```sh
./gradlew build
```

## Run

### Liquibase (manually)

Normally Liquibase is run on application startup but it cannot do so for native images due to support not having been
implemened in Liquibase (nor is it implemented for FlyWay).
Due to this it needs to be run manually for native images:

```sh
liquibase \
  --changeLogFile=src/main/resources/db/changelog/db.changelog-master.yaml update \
  --url jdbc:postgresql://localhost:5432/mydb \
  --username admin123 \
  --password admin123
```

### JVM

```sh
./gradlew bootRun
```

### Native (GraalVM)
#### Arch Linux
Arch Linux is rightly of the opinion that JAVA_HOME should not be used but to get native builds to work it is necessary.
https://bbs.archlinux.org/viewtopic.php?id=213095

##### Set java version (if necessary)

```sh
archlinux-java set java-22-graalvm-ee
```
#### Compile

```sh
set -x JAVA_HOME /usr/lib/jvm/java-22-graalvm-ee/
sudo -E ./gradlew nativeCompile

# or
sudo env JAVA_HOME=/usr/lib/jvm/java-22-graalvm-ee/ ./gradlew nativeCompile
```

#### Run

```sh
set -x JAVA_HOME /usr/lib/jvm/java-22-graalvm-ee/
sudo -E ./gradlew bootBuildImage

# or
sudo env JAVA_HOME=/usr/lib/jvm/java-22-graalvm-ee/ ./gradlew bootBuildImage
```

### Swagger
Swagger at:
```sh
http://localhost:8080/swagger-ui.html
```

## Docker

### Build image

```sh
# jvm
docker build -t products_jvm -f Dockerfile .

# native
docker build -t products_native -f Dockerfile.native_build .
```

### Run image

Make sure the database is running
```sh
docker-compose -f docker-compose-db.yml up -d
```

```sh
# jvm
docker run -p 8080:8080 products_jvm

# native
docker run -p 8080:8080 products_native
```
