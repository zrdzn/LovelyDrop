import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":spigot"))
    implementation(project(":v1_8"))
    implementation(project(":v1_12"))
    implementation(project(":v1_13"))

    compileOnly("org.spigotmc:spigot-api:1.8-R0.1-SNAPSHOT")

    implementation("dev.triumphteam:triumph-gui:3.1.5")
}

java {
    withSourcesJar()
}

tasks.withType<ShadowJar> {
    archiveFileName.set("LovelyDrop v${project.version}.jar")

    relocate("dev.triumphteam.gui", "io.github.zrdzn.minecraft.lovelydrop.gui")
}