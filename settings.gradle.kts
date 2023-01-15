pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    pluginManagement {
        val kotlinVersion: String = extra["kotlin.version"] as String
        val composeVersion: String = extra["compose.version"] as String

        plugins {
            kotlin("multiplatform") version kotlinVersion
            kotlin("plugin.serialization") version kotlinVersion
            id("org.jetbrains.compose") version composeVersion
        }
    }
}

rootProject.name = "ui"

