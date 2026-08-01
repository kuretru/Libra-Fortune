plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.kuretru.web"
version = "0.4.0"

val mybatisPlusVersion = "3.5.17"
val mapstructVersion = "1.6.3"
val springdocOpenapiVersion = "3.0.3"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    implementation("com.kuretru.microservices:galaxy-common:0.0.1-SNAPSHOT")
    implementation("com.kuretru.microservices:galaxy-web:0.0.1-SNAPSHOT")
    implementation("com.kuretru.microservices:galaxy-authentication:1.0.0")
    implementation("com.kuretru.microservices:galaxy-dashboard:0.0.1-SNAPSHOT")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.baomidou:mybatis-plus-spring-boot4-starter:${mybatisPlusVersion}")
    implementation("com.baomidou:mybatis-plus-jsqlparser:${mybatisPlusVersion}")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${springdocOpenapiVersion}")

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
