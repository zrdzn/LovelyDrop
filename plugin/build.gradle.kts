import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":spigot"))
    implementation(project(":v1_8"))
    implementation(project(":v1_12"))
    implementation(project(":v1_13"))

    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    val okaeriConfigs = "5.0.2"
    implementation("eu.okaeri:okaeri-configs-yaml-bukkit:$okaeriConfigs")
    implementation("eu.okaeri:okaeri-configs-serdes-bukkit:$okaeriConfigs")
    implementation("eu.okaeri:okaeri-configs-serdes-okaeri:$okaeriConfigs")
    implementation("eu.okaeri:okaeri-configs-validator-okaeri:$okaeriConfigs")

    implementation("org.slf4j:slf4j-reload4j:2.0.12")

    implementation("org.bstats:bstats-bukkit:3.0.2")

    implementation("com.zaxxer:HikariCP:4.0.3")

    implementation("dev.triumphteam:triumph-gui:3.1.10")

    implementation("de.tr7zw:item-nbt-api:2.13.1")
}

java {
    withSourcesJar()
}

tasks.withType<ProcessResources> {
    expand("version" to version)
}

tasks.withType<ShadowJar> {
    archiveFileName.set("lovelydrop-${project.version}.jar")

    val libsPath = "io.github.zrdzn.minecraft.lovelydrop.libs"
    relocate("org.bstats", "$libsPath.bstats")
    relocate("dev.triumphteam.gui", "$libsPath.gui")
    relocate("de.tr7zw.changeme.nbtapi", "$libsPath.nbtapi")
}