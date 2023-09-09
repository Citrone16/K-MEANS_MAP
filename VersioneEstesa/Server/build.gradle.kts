plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(enforcedPlatform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("mysql:mysql-connector-java:8.0.29")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType(Jar::class) {
    manifest {
        attributes["Main-Class"] = "MultiServer"
    }
}