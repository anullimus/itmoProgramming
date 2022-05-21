     plugins {
         java
         id("com.github.johnrengelman.shadow") version "7.1.2"
     }

    group = "com.company"
    version = "1.0"
    val mainClass = "$group.run.App"

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
        implementation("com.google.code.gson:gson:2.7")

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

    tasks.jar {
        manifest {
            attributes(
                "Manifest-Version" to "1.0",
                "Main-Class" to mainClass
            )
        }
    }

     tasks.shadowJar {
         archiveClassifier.set("shadow")
     }
