plugins {
    id("idea")
    id("maven-publish")
    id("net.neoforged.moddev") version "0.1.110"
    id("java-library")
}


val MINECRAFT_VERSION: String by rootProject.extra
val NEOFORGE_VERSION: String by rootProject.extra
val MOD_VERSION: String by rootProject.extra

base {
    archivesName = "iris-neoforge"
}

sourceSets {
}

repositories {
    maven {
        url = uri("https://maven.pkg.github.com/ims212/Forge_Fabric_API")
        credentials {
            username = "IMS212"
            // Read only token
            password = "ghp_" + "DEuGv0Z56vnSOYKLCXdsS9svK4nb9K39C1Hn"
        }
    }
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = uri("https://api.modrinth.com/maven")
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
    maven { url = uri("https://maven.neoforged.net/releases/") }
    maven { url = uri("https://maven.blamejared.com") }

}

tasks.jar {
    from(rootDir.resolve("LICENSE")) }

neoForge {
    // Specify the version of NeoForge to use.
    version = NEOFORGE_VERSION

    runs {
        create("client") {
            additionalRuntimeClasspath.add("io.github.douira:glsl-transformer:2.0.1")
            additionalRuntimeClasspath.add("org.anarres:jcpp:1.4.14")
            client()
        }
    }

    mods {
        create("sodium") {
            sourceSet(sourceSets.main.get())
        }
    }

    mods {
        create("embeddium") {
            sourceSet(sourceSets.main.get())
        }
    }
}

val localRuntime = configurations.create("localRuntime")

val SODIUM_PATH = "sodium-neoforge-0.6.0-alpha.1-modonly.jar"

dependencies {
    compileOnly(project(":common"))

    implementation("io.github.douira:glsl-transformer:2.0.1")
    jarJar("io.github.douira:glsl-transformer:[2.0.1,2.0.2]") {
        isTransitive = false
    }
    implementation("org.anarres:jcpp:1.4.14")
    jarJar("org.anarres:jcpp:[1.4.14,1.4.15]") {
        isTransitive = false
    }
    if (!rootDir.resolve("custom_sodium").resolve(SODIUM_PATH).exists()) {
        throw IllegalStateException("Sodium jar doesn't exist!!! It needs to be at $SODIUM_PATH")
    }
    compileOnly(files(rootDir.resolve("custom_sodium").resolve(SODIUM_PATH)))
    implementation("org.embeddedt:embeddium-1.21:1.0.0-beta.1+mc1.21")

    compileOnly(files(rootDir.resolve("DHApi.jar")))
}

// Sets up a dependency configuration called 'localRuntime'.
// This configuration should be used instead of 'runtimeOnly' to declare
// a dependency that will be present for runtime testing but that is
// "optional", meaning it will not be pulled by dependents of this mod.
configurations {
    runtimeClasspath.get().extendsFrom(localRuntime)
}

// NeoGradle compiles the game, but we don't want to add our common code to the game's code
val notNeoTask: (Task) -> Boolean = { it: Task -> !it.name.startsWith("neo") && !it.name.startsWith("compileService") }

tasks.withType<JavaCompile>().matching(notNeoTask).configureEach {
    source(project(":common").sourceSets.main.get().allSource)
    source(project(":common").sourceSets.getByName("vendored").allSource)
    source(project(":common").sourceSets.getByName("sodiumCompatibility").allSource)
    source(project(":common").sourceSets.getByName("embeddiumCompatibility").allSource)
}

tasks.withType<Javadoc>().matching(notNeoTask).configureEach {
    source(project(":common").sourceSets.main.get().allJava)
}

tasks.withType<ProcessResources>().matching(notNeoTask).configureEach {
    from(project(":common").sourceSets.main.get().resources)
    from(project(":common").sourceSets.getByName("sodiumCompatibility").resources)
    from(project(":common").sourceSets.getByName("embeddiumCompatibility").resources)
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

publishing {
    publications {

    }
    repositories {
        maven {
            url = uri("file://" + System.getenv("local_maven"))
        }
    }
}
