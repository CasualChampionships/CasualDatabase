rootProject.name = "CasualDatabase"

enableFeaturePreview("VERSION_CATALOGS")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}