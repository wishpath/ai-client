plugins {
    id("java")
}

group = "org.sa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20231013")
}

tasks.test {
    useJUnitPlatform()
}