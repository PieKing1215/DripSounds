import org.gradle.kotlin.dsl.annotationProcessor

plugins {
    id("net.neoforged.moddev.legacyforge") version "2.0.147"
}

version = "${property("mod.version")}+${sc.current.version}"
base.archivesName = "${property("mod.id") as String}-forge"

val requiredJava = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
    sc.current.parsed >= "1.17" -> JavaVersion.VERSION_16
    else -> JavaVersion.VERSION_1_8
}

repositories {
    /**
     * Restricts dependency search of the given [groups] to the [maven URL][url],
     * improving the setup speed.
     */
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }
    strictMaven("https://www.cursemaven.com", "CurseForge", "curse.maven")
    strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")
    strictMaven("https://maven.shedaniel.me/", "shedaniel", "me.shedaniel.cloth")
}

dependencies {
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor");

    modImplementation("me.shedaniel.cloth:cloth-config-forge:${property("deps.cloth")}")
}

mixin {
    add(sourceSets.main.get(), "${property("mod.id") as String}.mixins.refmap.json")
    config("${property("mod.id") as String}.mixins.json")
}

//jar {
//    manifest.attributes([
//        "MixinConfigs": "${property("mod.id") as String}.mixins.json"
//    ])
//}

legacyForge {
    version = property("deps.forge") as String

    mods {
        register(property("mod.id") as String) {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        register("client") {
            gameDirectory = file("../../run/")
            client()
        }

        register("server") {
            gameDirectory = file("../../run/")
            server()
        }
    }
}

java {
//    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava

    toolchain {
        languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
    }
}

tasks {
    processResources {
        fun MutableMap<String, String>.register(key: String, value: String) {
            inputs.property(key, value)
            set(key, value)
        }

        val props = buildMap {
            register("id", sc.properties["mod.id"])
            register("name", sc.properties["mod.name"])
            register("version", sc.properties["mod.version"])
            register("description", sc.properties["mod.description"])
            register("authors", sc.properties["mod.authors"])
            register("license", sc.properties["mod.license"])
            register("repo", sc.properties["mod.repo"])
            register("mc_min", sc.properties["mod.mc_min"])
            register("forge_min", sc.properties["mod.forge_min"])
            register("cloth_min", sc.properties["mod.cloth_min"])
            register("cloth_id", if (sc.current.parsed >= "1.18") "cloth-config" else "cloth-config2")
        }

        filesMatching("META-INF/mods.toml") { expand(props) }

        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }

        exclude("fabric.mod.json", "*.ct", "*.classtweaker")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    // Includes the license file in the built mod
    withType<Jar> {
        val name = project.property("mod.id")
        inputs.property("mod_id", name)
        from("../../LICENSE") { rename { "$it-$name" } }
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds mod jars and copies results to `build/libs/{mod version}/`"

        inputs.property("version", project.property("mod.version"))
        from(jar.flatMap { it.archiveFile }/*, named<Jar>("sourcesJar").flatMap { it.archiveFile }*/)
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        rename(".+", "${project.property("mod.name")}-${project.property("mod.version")}+${project.name.replace('f', 'F').replace('n', 'N')}.jar")
    }

    if (stonecutter.current.isActive) {
        rootProject.tasks.register("Run Active Client") {
            group = "project"
            dependsOn(":${stonecutter.current.project}:runClient")
        }
    }
}
