plugins {
    java
    id("org.springframework.boot") version "4.0.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.kuretru.web"
version = "0.4.0"

val mybatisPlusVersion = "3.5.16"
val guavaVersion = "33.5.0-jre"
val mapstructVersion = "1.6.3"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    implementation("com.kuretru.microservices:galaxy-common:0.0.1-SNAPSHOT")
    implementation("com.kuretru.microservices:galaxy-web:0.0.1-SNAPSHOT")
    implementation("com.kuretru.microservices:galaxy-authentication:1.0.0")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation ("com.baomidou:mybatis-plus-spring-boot4-starter:${mybatisPlusVersion}")
    implementation("com.baomidou:mybatis-plus-jsqlparser:${mybatisPlusVersion}")
    implementation("com.google.guava:guava:$guavaVersion")

    runtimeOnly("com.mysql:mysql-connector-j")

    compileOnly("org.projectlombok:lombok")
    compileOnly("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testCompileOnly("org.projectlombok:lombok")
    testCompileOnly("org.mapstruct:mapstruct:$mapstructVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testAnnotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
