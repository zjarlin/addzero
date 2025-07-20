import org.babyfish.jimmer.Vars.jvmMainKspBuildMetaDataDir
import org.babyfish.jimmer.Vars.jvmMainSourceDir

plugins {
    id("kotlin-convention")

    id("com.google.devtools.ksp")
    id("ksp4jdbc")
    id("ksp4iso")
    id("ksp4projectdir")
}

dependencies {
    ksp(libs.jimmer.ksp)
    ksp(projects.lib.addzeroJdbc2entityProcessor)
    ksp(projects.lib.addzeroEntity2isoProcessor)
    ksp(projects.lib.addzeroEntity2formProcessor)
    ksp(projects.lib.addzeroEntity2mcpProcessor)
    implementation(libs.jimmer.sql.kotlin)
    //easy-excel
    implementation(libs.fastexcel)
//    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//    implementation ("cn.hutool:hutool-core:${libs.versions.hutoolAll}" )
    // 仅注解
//    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    //实体表单核心注解
    implementation(projects.lib.addzeroEntity2formCore)
    implementation(projects.shared)
    implementation(libs.hutool.all)
}
val modelProject = project(":backend:model")
val modelSourceDir = modelProject.projectDir.resolve(jvmMainSourceDir).absolutePath
val modelBuildDir = modelProject.projectDir.resolve(jvmMainKspBuildMetaDataDir).absolutePath
ksp {

    arg("modelSourceDir", modelSourceDir)
    arg("modelBuildDir", modelBuildDir)
    arg("modelPackageName", "com.addzero.model.entity")
}