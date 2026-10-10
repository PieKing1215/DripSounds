import me.modmuss50.mpp.ReleaseType

plugins {
    id("dev.kikugie.stonecutter")

    // https://github.com/modmuss50/mod-publish-plugin
    id("me.modmuss50.mod-publish-plugin") version "2.2.1"
}

stonecutter active "26.3-fabric"

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    val (version, loader) = current.project.split('-', limit = 2)

    // Makes version- and loader-specific properties apply from `stoncutter.properties.toml`
    properties {
        tags(version, loader)
    }

    // Adds constants to Stonecutter comments (i.e. for `//? if fabric {...`)
    constants {
        match(loader, "fabric", "neoforge", "forge")
    }

    swaps["mod_version"] = "\"${properties.get<String>("mod.version")}\";"
    swaps["minecraft"] = "\"${node.metadata.version}\";"
    swaps["modid"] = "\"${properties.get<String>("mod.id")}\";"
    constants["release"] = properties.get<String>("mod.id") != "template"
    dependencies["fapi"] = properties.getOrNull<String>("deps.fabric_api") ?: "0"

    replacements {
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
        }

        string(current.parsed >= "26.1") {
            replace("classTweaker v2 named", "classTweaker v2 official")
        }
    }
}

stonecutter tasks {
    val ordering = versionComparator.thenComparingInt { if (it.metadata.project.endsWith("fabric")) 1 else 0 }

    order("publishGithub", ordering)
    order("publishModrinth", ordering)
    order("publishCurseforge", ordering)
}

version = "${property("mod.version")}"

publishMods {
    changelog = providers.environmentVariable("RELEASE_CHANGELOG").orElse("TEST")
    type = providers.environmentVariable("RELEASE_TYPE").orElse("ALPHA").map(ReleaseType::of)

    var dry = providers.environmentVariable("DRY_RUN").getOrElse("true").toBoolean()
    var gh = providers.environmentVariable("RELEASE_GITHUB").getOrElse("false").toBoolean()
    var mr = providers.environmentVariable("RELEASE_MODRINTH").getOrElse("false").toBoolean()
    var cf = providers.environmentVariable("RELEASE_CURSEFORGE").getOrElse("false").toBoolean()

    println("DRY_RUN = $dry")
    println("RELEASE_GITHUB = $gh")
    println("RELEASE_MODRINTH = $mr")
    println("RELEASE_CURSEFORGE = $cf")

    if (gh) {
        if (!providers.environmentVariable("GITHUB_TOKEN").isPresent()) {
            throw GradleException("Missing GITHUB_TOKEN")
        }

        github {
            accessToken = providers.environmentVariable("GITHUB_TOKEN").get()
            repository = "${property("mod.repo")}".replace("https://github.com/", "")
            commitish = "main"
            tagName = "v${property("mod.version")}"
            displayName = "v${property("mod.version")}"

            // files added by subprojects
            allowEmptyFiles = true
        }
    }

    if (mr) {
        if (!providers.environmentVariable("MODRINTH_API_KEY").isPresent()) {
            throw GradleException("Missing MODRINTH_API_KEY")
        }
    }

    if (cf) {
        if (!providers.environmentVariable("CURSEFORGE_API_KEY").isPresent()) {
            throw GradleException("Missing CURSEFORGE_API_KEY")
        }
    }

    dryRun = providers.environmentVariable("DRY_RUN").getOrElse("true").toBoolean()
}

subprojects {
    apply {
        plugin("me.modmuss50.mod-publish-plugin")
    }

    val (mc, loader) = project.name.split('-', limit = 2)

    ext.set("jarName", "${project.property("mod.name")}-${project.property("mod.version")}+${project.name.replace('f', 'F').replace('n', 'N')}.jar")

    publishMods {
        file = rootProject.file("build/libs/${project.property("mod.version")}/${property("jarName")}")
        modLoaders.add(loader)

        changelog = rootProject.publishMods.changelog
        type = rootProject.publishMods.type

        if (providers.environmentVariable("RELEASE_GITHUB").getOrElse("false").toBoolean()) {
            github {
                accessToken = providers.environmentVariable("GITHUB_TOKEN").get()

                parent(rootProject.tasks.named("publishGithub"))
            }
        }

        if (providers.environmentVariable("RELEASE_MODRINTH").getOrElse("false").toBoolean()) {
            modrinth {
                accessToken = providers.environmentVariable("MODRINTH_API_KEY").get()
                projectId = project.property("modrinth_project") as String

                displayName = "${project.property("mod.version")} for ${loader.replace('f', 'F').replace('n', 'N')} ${project.property("mod.display_mc_version")}"
                version = "${project.property("mod.version")}+${project.name}"

                minecraftVersionRange {
                    start = project.property("mod.mc_min") as String
                    end = mc
                }

                requires("cloth-config")

                if (loader == "fabric") {
                    optional("modmenu")
                }
            }
        }

        if (providers.environmentVariable("RELEASE_CURSEFORGE").getOrElse("false").toBoolean()) {
            curseforge {
                accessToken = providers.environmentVariable("CURSEFORGE_API_KEY").get()
                projectId = project.property("curseforge_project") as String

                displayName = "${project.property("mod.version")} for ${loader.replace('f', 'F').replace('n', 'N')} ${project.property("mod.display_mc_version")}"
                version = "${project.property("mod.version")}+${project.name}"

                client = true
                server = false

                minecraftVersionRange {
                    start = project.property("mod.mc_min") as String
                    end = mc
                }

                requires("cloth-config")

                if (loader == "fabric") {
                    optional("modmenu")
                }
            }
        }

        dryRun = rootProject.publishMods.dryRun
    }

    tasks {
        register("publishAndCollect") {
            group = "publishing"
            description = "Copies publishMods results to `build/publishMods/{mod version}/`"

            dependsOn("publishMods")

            doLast {
                copy {
                    from(file("build/publishMods/"))
                    into(rootProject.layout.buildDirectory.file("publishMods/"))
                }
            }
        }
    }
}
