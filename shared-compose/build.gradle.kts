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

                       // 组件库依赖
//            implementation("site.addzero:addzero-compose-native-component:+")
            implementation("site.addzero:addzero-compose-native-component-assist:+")
            implementation("site.addzero:addzero-compose-native-component-button:+")
            implementation("site.addzero:addzero-compose-native-component-card:+")
            implementation("site.addzero:addzero-compose-native-component-form:+")
            implementation("site.addzero:addzero-compose-native-component-high-level:+")
            implementation("site.addzero:addzero-compose-native-component-hook:+")
            implementation("site.addzero:addzero-compose-native-component-searchbar:+")
            implementation("site.addzero:addzero-compose-native-component-select:+")
            implementation("site.addzero:addzero-compose-native-component-table:+")
            implementation("site.addzero:addzero-compose-native-component-table-core:+")
            implementation("site.addzero:addzero-compose-native-component-table-pro:+")
            implementation("site.addzero:addzero-compose-native-component-tree:+")





            implementation("site.addzero:addzero-tool:+")
            implementation("site.addzero:addzero-jimmer-model-lowquery:+")
            implementation(projects.shared)
            implementation("site.addzero:addzero-network-starter:+")
        }
    }
}
