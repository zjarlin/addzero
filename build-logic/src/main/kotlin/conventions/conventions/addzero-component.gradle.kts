plugins {
    id("org.jetbrains.kotlin.multiplatform")
}
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()
kotlin {
    dependencies {
        implementation("site.addzero:addzero-compose-native-component:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:addzero-compose-native-component-assist:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:addzero-compose-native-component-button:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:addzero-compose-native-component-card:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:addzero-compose-native-component-form:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:addzero-compose-native-component-high-level:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:addzero-compose-native-component-hook:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:addzero-compose-native-component-searchbar:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:addzero-compose-native-component-select:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:addzero-compose-native-component-table:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:addzero-compose-native-component-table-core:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:addzero-compose-native-component-table-pro:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:addzero-compose-native-component-tree:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:addzero-compose-native-component-hook:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:addzero-compose-klibs-component:${libs.versions.addzeroLTSVersion.get()}")

    }
}
