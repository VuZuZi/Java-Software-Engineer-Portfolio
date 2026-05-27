plugins {
    id("java")
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.lge"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // ===============================
    // 🌱 SPRING BOOT STARTERS
    // ===============================
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
//    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
//    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-devtools")

    // ===============================
    // 🔥 FIX QUAN TRỌNG (CHO sec:authorize)
    // ===============================
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")

    // ===============================
    // 🗄️ DATABASE
    // ===============================
    implementation("com.mysql:mysql-connector-j:8.2.0")

    // ===============================
    // ☁️ CLOUDINARY
    // ===============================
    implementation("com.cloudinary:cloudinary-http44:1.33.0")

    // ===============================
    // ⚙️ LOMBOK
    // ===============================
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // ===============================
    // 🔐 JWT (BẮT BUỘC)
    // ===============================
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // ===============================
    // 🧪 TEST
    // ===============================
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}