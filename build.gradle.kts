plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

allprojects {
    group = "io.github.zrdzn.minecraft"
    version = "1.4.2-SNAPSHOT"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.github.johnrengelman.shadow")

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
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
    }
}