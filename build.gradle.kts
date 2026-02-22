plugins {
    kotlin("jvm") version "1.9.0"
    id("io.qameta.allure") version "2.11.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")
    testImplementation("com.squareup.okhttp3:okhttp:4.11.0")
    testImplementation("io.qameta.allure:allure-junit5:2.21.0")
}

tasks.test {
    useJUnitPlatform()
}

allure {
    report { version.set("2.21.0") }
    adapter {
        aspectjWeaver.set(true)
        frameworks { junit5 { adapterVersion.set("2.21.0") } }
    }
}
