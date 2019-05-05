import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val kotlinVersion = "1.3.31"
val http4kVersion = "3.140.0"

plugins {
    kotlin("jvm") version "1.3.31"
    id("com.github.johnrengelman.shadow") version "5.0.0"
    id("com.palantir.git-version") version "0.12.0-rc2"
}
val gitVersion: groovy.lang.Closure<*> by extra

group = "com.batchofcode"
version = gitVersion.call()
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

tasks.withType<ShadowJar> {
    classifier = ""
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.batchofcode.runtimelocal.handler.RootHandlerKt"
    }
}