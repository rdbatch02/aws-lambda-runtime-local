import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.3.31"
val http4kVersion = "3.140.0"

plugins {
    kotlin("jvm") version "1.3.31"
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

group = "com.batchofcode"
version = "0.1-SNAPSHOT"
val artifactId = "aws-lambda-runtime-local"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile(group = "org.http4k", name = "http4k-core", version = http4kVersion)
    compile(group = "org.http4k", name = "http4k-server-apache", version = http4kVersion)
    compile(group = "org.http4k", name = "http4k-format-jackson", version = http4kVersion)

    testCompile(kotlin("test"))
    testCompile(kotlin("test-junit"))
    testImplementation("io.mockk:mockk:1.9.3")
    testCompile(group = "org.http4k", name = "http4k-testing-hamkrest", version = http4kVersion)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.batchofcode.runtimelocal.handler.RootHandlerKt"
    }
}