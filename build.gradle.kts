import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    `maven-publish`
}

group = "com.github.niehm.detektrulenostringparameter"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.18.1")

    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.18.1")
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("junit:junit:4.13.2")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
