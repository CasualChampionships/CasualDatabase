plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)

    `maven-publish`
}

group = "net.casual-championships"
version = "0.2.0"

repositories {
    mavenCentral()
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

tasks.shadowJar {
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
    }
}

publishing {
    publications {
        create<MavenPublication>("CasualDatabase") {
            groupId = "net.casual-championships"
            artifactId = "casual-database"

            from(components["java"])
            artifact(tasks.kotlinSourcesJar) {
                classifier = "sources"
            }
        }
        create<MavenPublication>("CasualDatabaseCore") {
            groupId = "net.casual-championships"
            artifactId = "casual-database-core"

            artifact(tasks.shadowJar.get()) {
                classifier = null
            }
        }
    }

    repositories {
        val mavenUrl = System.getenv("MAVEN_URL")
        if (mavenUrl != null) {
            maven {
                url = uri(mavenUrl)
                val mavenUsername = System.getenv("MAVEN_USERNAME")
                val mavenPassword = System.getenv("MAVEN_PASSWORD")
                if (mavenUsername != null && mavenPassword != null) {
                    credentials {
                        username = mavenUsername
                        password = mavenPassword
                    }
                }
            }
        }
    }
}