import org.gradle.internal.impldep.com.google.api.services.storage.Storage
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    id("com.google.devtools.ksp")
}
ksp {


}

// 确保所有 Kotlin 编译任务都依赖于 shared 模块的 KSP 元数据生成
//tasks.withType<KotlinCompilationTask<*>>().configureEach {
//    if (name != "kspCommonMainKotlinMetadata") {
//        dependsOn(":shared:kspCommonMainKotlinMetadata")
//    }
//}
