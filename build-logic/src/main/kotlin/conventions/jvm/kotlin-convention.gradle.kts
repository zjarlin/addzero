import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-convention")
    kotlin("jvm")
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
       jvmTarget.set(provider { JvmTarget.fromTarget(java.targetCompatibility.toString()) })
        freeCompilerArgs.set(listOf("-Xjsr305=strict", "-Xjvm-default=all"))
    }
}
