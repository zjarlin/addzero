import de.jensklingenberg.ktorfit.gradle.KtorfitPluginExtension
import org.babyfish.jimmer.Vars.commonMainKspBuildMetaDataDir
import org.babyfish.jimmer.Vars.commonMainSourceDir
import org.babyfish.jimmer.Vars.jvmMainKspBuildMetaDataDir
import org.babyfish.jimmer.Vars.jvmMainSourceDir
import org.gradle.kotlin.dsl.withType

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    id("kmp-shared")
    // 暂时完全移除 KSP 插件，避免依赖顺序问题
     id("ksp4shared-convention")
    id("de.jensklingenberg.ktorfit") version "+"
}
configure<KtorfitPluginExtension> {
    kotlinVersion.set("2.3.0")
}



dependencies {

    // 暂时注释掉枚举生成，避免依赖顺序问题
     kspCommonMainMetadata(projects.lib.addzeroJdbc2enumProcessor)

    // 暂时注释掉 API 提供者处理器，避免依赖顺序问题
     kspCommonMainMetadata(projects.lib.addzeroApiproviderProcessor)

//    with(projects.lib.addzeroJdbc2enumProcessor) {
//        add("kspCommonMainMetadata", this)
//    }

//    with(projects.lib.addzeroApiproviderProcessor) {
//        add("kspCommonMainMetadata", this)
//    }


}



val backendProject = project(":backend")

val composeProject = project(":composeApp")
val sharedProject = project(":shared")


val backendSourceDir = backendProject.projectDir.resolve(jvmMainSourceDir).absolutePath
val backendBuildDir = backendProject.projectDir.resolve(jvmMainKspBuildMetaDataDir).absolutePath

val composeSourceDir = composeProject.projectDir.resolve(commonMainSourceDir).absolutePath
val composeBuildDir = composeProject.projectDir.resolve(commonMainKspBuildMetaDataDir).absolutePath

val sharedSourceDir = sharedProject.projectDir.resolve(commonMainSourceDir).absolutePath
val sharedBuildDir = sharedProject.projectDir.resolve(commonMainKspBuildMetaDataDir).absolutePath


ksp {
    arg("sharedSourceDir", sharedSourceDir)
    arg("sharedBuildDir", sharedBuildDir)
    // 枚举生成配置（生成到 shared 编译目录）
    arg("enumOutputPackage", "com.addzero.kmp.generated.enums")
    // JDBC 配置（用于枚举生成）
    arg("jdbcUrl", "jdbc:postgresql://localhost:15432/postgres")
    arg("jdbcUsername", "postgres")
    arg("jdbcPassword", "postgres")
    arg("jdbcSchema", "public")
    arg("jdbcDriver", "org.postgresql.Driver")
    // 字典枚举处理器配置
    arg("dictTableName", "sys_dict")
    arg("dictIdColumn", "id")
    arg("dictCodeColumn", "dict_code")
    arg("dictNameColumn", "dict_name")
    arg("dictItemTableName", "sys_dict_item")
    arg("dictItemForeignKeyColumn", "dict_id")
    arg("dictItemCodeColumn", "item_value")
    arg("dictItemNameColumn", "item_text")
}



kotlin {
    sourceSets {
        commonMain.dependencies {

            implementation(libs.kotlinx.datetime)

            implementation(projects.lib.addzeroTool)


            implementation(libs.ktorfit.lib)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            //懒人http
//            implementation(libs.lazy.people.http)
            androidMain.dependencies {
                implementation(libs.ktor.client.cio)
            }
            jvmMain.dependencies {
                implementation(libs.ktor.client.cio)
            }
            wasmJsMain.dependencies {
                implementation(libs.ktor.client.js)
            }

        }
    }
}
