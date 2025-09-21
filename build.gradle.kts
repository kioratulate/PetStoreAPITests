plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.4.21"
    id("io.qameta.allure") version "2.11.2"
}

allure {
    version.set("2.24.0")
    adapter {
        frameworks {
            junit5 {
                adapterVersion.set("2.24.0")
            }
        }
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}



dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // HTTP Client
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("io.mockk:mockk:1.13.8")

    testImplementation("io.qameta.allure:allure-junit5:2.24.0")

    //Logging
    testImplementation("ch.qos.logback:logback-classic:1.4.14")
}


tasks.test {
    useJUnitPlatform()
}