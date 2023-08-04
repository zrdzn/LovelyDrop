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
    compileOnly("org.bstats:bstats-bukkit:3.0.2")

    implementation("dev.triumphteam:triumph-gui:3.1.4")
}

java {
    withSourcesJar()
}

tasks.withType<ShadowJar> {
    archiveFileName.set("LovelyDrop v${project.version}.jar")

    val libsPackage = "io.github.zrdzn.minecraft.lovelydrop"
    relocate("org.bstats", "$libsPackage.bstats")
    relocate("dev.triumphteam.gui", "$libsPackage.gui")
}