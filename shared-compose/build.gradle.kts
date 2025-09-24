plugins {
    id("kmp-lib")
    id("kmp-ksp-plugin")
    id("kmp-ktorfit")
    id("addzero-component")
    id("kmp-json-withtool")
    id("kmp-datetime")
}
dependencies {
    kspCommonMainMetadata("site.addzero:addzero-apiprovider-processor:+")
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("site.addzero:addzero-tool:+")
    implementation("site.addzero:addzero-jimmer-model-lowquery:+")
            implementation(projects.shared)
    implementation("site.addzero:addzero-network-starter:+")
        }
    }
}
