import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.3.31"
val http4kVersion = "3.140.0"

plugins {
    kotlin("jvm") version "1.3.31"
}

group = "com.batchofcode"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile(group = "org.http4k", name = "http4k-core", version = http4kVersion)
    compile(group = "org.http4k", name = "http4k-server-apache", version = http4kVersion)
    compile(group = "org.http4k", name = "http4k-format-jackson", version = http4kVersion)

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("io.mockk:mockk:1.9.3")
    testCompile(group = "org.http4k", name = "http4k-testing-hamkrest", version = http4kVersion)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}