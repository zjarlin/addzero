plugins {
    id("org.jetbrains.kotlin.multiplatform")
}
// 自动查找所有 addzero-compose-native-component- 开头的子模块
val componentModules = rootProject.subprojects
    .filter {
        it.name.startsWith("addzero-compose-native-component-") &&
                it.name != "addzero-compose-native-component"
//            &&
//        it.name != "addzero-compose-native-component-table-pro"
    }
    .map { it }
//componentModules.forEach {
//    println("揭晓的模块${it.name}")
//}

kotlin {
    sourceSets {
        commonMain.dependencies {
//            implementation(projects.lib.toolKmp.addzeroToolJson)
//            implementation(projects.lib.toolKmp.addzeroTool)

            componentModules.forEach {
                implementation(it)
            }

        }
    }
}
