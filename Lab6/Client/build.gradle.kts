plugins {
    java
    id("com.github.johnrengelman.shadow") version ("7.1.2")
}

group = "com.company.Lab6"
version = "2.1"
val mainClass = "clientLogic.ClientSide"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("com.google.code.gson:gson:2.7")
    implementation(project(":General"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.javadoc {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    archiveClassifier.set("")
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "Main-Class" to mainClass
        )
    }
}

