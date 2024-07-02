import org.apache.commons.io.output.ByteArrayOutputStream
import java.nio.charset.Charset

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.serialization") version "1.9.24"

    id("com.github.johnrengelman.shadow") version "7.1.2"

    `maven-publish`
}

group = "net.casual"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(libs.mysql)
    implementation(libs.hikari)

    api(libs.exposed.core)
    api(libs.exposed.dao)
    api(libs.exposed.jdbc)
    api(libs.exposed.datetime)
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "21"
}

tasks.shadowJar {
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
    }
}

publishing {
    publications {
        create<MavenPublication>("casual-database") {
            groupId = "com.github.CasualChampionships"
            artifactId = "casual-database"
            version = getGitHash()

            from(components["java"])
            artifact(tasks.kotlinSourcesJar) {
                classifier = "sources"
            }
        }
        create<MavenPublication>("casual-database-core") {
            groupId = "com.github.CasualChampionships"
            artifactId = "casual-database-core"
            version = getGitHash()

            artifact(tasks.shadowJar.get()) {
                classifier = null
            }
        }
    }
}

fun getGitHash(): String {
    val out = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "HEAD")
        standardOutput = out
    }
    return out.toString(Charset.defaultCharset()).trim()
}