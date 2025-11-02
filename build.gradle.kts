plugins {
    id("fabric-loom") version "1.12-SNAPSHOT"
    id("maven-publish")
}

class Loader {
    val version = property("loader_version")
    val fabricVersion = property("fabric_version")
}

class Mod {
    val version = property("mod_version").toString()
    val loader = Loader()
    val minecraftVersion = property("minecraft_version")
    val description = property("mod_description").toString()
    val mavenGroup = property("maven_group").toString()
    val archivesBaseName = property("archives_base_name").toString()
}

val mod = Mod()
version = mod.version
group = mod.mavenGroup

base {
    archivesName = mod.archivesBaseName
}

loom {
    runConfigs.remove(runConfigs["server"])
}

repositories {
}

dependencies {
    minecraft("com.mojang:minecraft:${mod.minecraftVersion}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${mod.loader.version}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${mod.loader.fabricVersion}")
}

tasks.processResources {
    val props = buildMap {
        put("version", mod.version)
        put("minecraft_version", mod.minecraftVersion)
        put("loader_version", mod.loader.version)
        put("mod_description", mod.description)
    }
    props.forEach(inputs::property)
    filteringCharset = "UTF-8"
    filesMatching("fabric.mod.json") {
        expand(props)
    }
}

val targetJavaVersion = 21
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10) {
        options.release.set(targetJavaVersion)
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${mod.archivesBaseName}" }
    }
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = mod.archivesBaseName
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
    }
}
