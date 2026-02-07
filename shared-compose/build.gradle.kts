plugins {
    id("site.addzero.buildlogic.kmp.composition.kmp-lib")
    id("site.addzero.buildlogic.kmp.libs.kmp-ksp-plugin")
//    id("site.addzero.buildlogic.kmp.libs.kmp-ktorfit")
    id("site.addzero.buildlogic.conventions.addzero-component")
    id("site.addzero.buildlogic.kmp.composition.kmp-json-withtool")
    id("site.addzero.buildlogic.kmp.libs.kmp-datetime")
}
dependencies {
    kspCommonMainMetadata("site.addzero:apiprovider-processor:${libs.versions.addzeroLTSVersion.get()}")
}

kotlin {
dependencies {
            implementation("site.addzero:tool:${libs.versions.addzeroLTSVersion.get()}")
            implementation("site.addzero:jimmer-model-lowquery:${libs.versions.addzeroLTSVersion.get()}")
            implementation(projects.shared)
            implementation("site.addzero:network-starter:${libs.versions.addzeroLTSVersion.get()}")
        }
    }
