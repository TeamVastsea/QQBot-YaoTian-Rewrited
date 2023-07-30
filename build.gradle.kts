plugins {
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.serialization") version "1.8.22"
    id("net.mamoe.mirai-console") version "2.15.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")
    implementation("com.rabbitmq:amqp-client:5.18.0")
    testImplementation(kotlin("test"))
}