plugins {
    id("kmp-lib")
    id("kmp-ksp-plugin")
    id("kmp-ktorfit")
    id("addzero-component")
    id("kmp-json-withtool")
    id("kmp-datetime")
}
dependencies {
    kspCommonMainMetadata("site.addzero:addzero-apiprovider-processor:${libs.versions.addzeroLTSVersion.get()}")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("site.addzero:addzero-tool:${libs.versions.addzeroLTSVersion.get()}")
            implementation("site.addzero:addzero-jimmer-model-lowquery:${libs.versions.addzeroLTSVersion.get()}")
            implementation(projects.shared)
            implementation("site.addzero:addzero-network-starter:${libs.versions.addzeroLTSVersion.get()}")
        }
    }
}
