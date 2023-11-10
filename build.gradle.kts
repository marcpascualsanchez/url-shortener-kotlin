import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import Build_gradle.Versions.cucumber
import Build_gradle.Versions.junit
import Build_gradle.Versions.wiremock

plugins {
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	id("org.jetbrains.kotlin.plugin.jpa") version "1.7.10"
}

group = "com.marcpascualsanchez"
version = "0.0.1-SNAPSHOT"

object Versions {
	const val cucumber = "7.8.1"
	const val wiremock = "2.27.2"
	const val junit = "5.4.0"
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenLocal()
	maven("https://packages.confluent.io/maven/")
}

configurations {
	all {
		exclude( group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
	}
	testImplementation {
		exclude( group = "ch.qos.logback", module = "logback-classic")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.data:spring-data-redis")
	implementation("org.redisson:redisson:3.22.1")
	implementation("io.lettuce:lettuce-core")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.github.tomakehurst:wiremock:$wiremock")
	testImplementation("io.cucumber:cucumber-java:$cucumber")
	testImplementation("io.cucumber:cucumber-junit:$cucumber")
	testImplementation("io.cucumber:cucumber-spring:$cucumber")
	testImplementation("org.junit.jupiter:junit-jupiter:$junit")
	testImplementation("it.ozimov:embedded-redis:0.7.3")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
