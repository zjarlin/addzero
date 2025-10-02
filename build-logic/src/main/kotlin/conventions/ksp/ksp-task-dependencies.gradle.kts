import com.google.devtools.ksp.gradle.KspAATask
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * 这个文件定义了KSP任务的通用依赖配置
 * 解决KSP生成的文件被其他任务使用但没有声明依赖的问题
 */

// 为所有Kotlin编译任务添加对KSP任务的依赖
tasks.withType<KotlinCompile>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

// 为所有Kotlin JS编译任务添加对KSP任务的依赖
tasks.withType<Kotlin2JsCompile>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

// 为所有Jar任务添加对KSP任务的依赖
tasks.withType<Jar>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

// 为所有KSP任务添加对主KSP任务的依赖
tasks.withType<KspAATask>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

// 为所有sourcesJar任务添加对KSP任务的依赖
tasks.configureEach {
    if (name.contains("sourcesJar")) {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}