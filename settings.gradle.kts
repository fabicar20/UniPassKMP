rootProject.name = "UniPassKMP"

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

// Módulos del proyecto:
//  :shared     -> lógica compartida (dominio, datos, red, navegación, ViewModels)
//  :composeApp -> interfaz (Compose Multiplatform) + arranque en Android y Desktop
include(":shared")
include(":composeApp")
