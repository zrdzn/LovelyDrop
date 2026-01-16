import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.diffplug.spotless") version "8.0.0"
}

allprojects {
    group = "io.github.zrdzn.minecraft.lovelydrop"
    version = "2.0.3-SNAPSHOT"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "com.diffplug.spotless")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    repositories {
        mavenCentral()

        maven {
            name = "spigot-repository"
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }

        maven {
            name = "sonatype-repository"
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }

        maven {
            name = "okaeri-repository"
            url = uri("https://storehouse.okaeri.eu/repository/maven-public/")
        }

        maven {
            name = "codemc-repository"
            url = uri("https://repo.codemc.io/repository/maven-public/")
        }
    }

    spotless {
        java {
            eclipse().configFile(rootProject.file("config/codestyle.xml"))
            target("src/**/*.java")
        }
    }

    tasks.withType<ShadowJar> {
        dependsOn("spotlessApply")
    }
}