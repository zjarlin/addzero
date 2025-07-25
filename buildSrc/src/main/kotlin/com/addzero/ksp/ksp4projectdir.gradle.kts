import com.addzero.Vars.jvmMainSourceDir
import com.addzero.Vars.jvmMainKspBuildMetaDataDir
import com.addzero.Vars.commonMainSourceDir
import com.addzero.Vars.commonMainKspBuildMetaDataDir
import com.addzero.Vars.commonMainSourceDir
import com.addzero.Vars.commonMainKspBuildMetaDataDir
// 计算各模块目录（使用常量字符串）


val serverProject = project(":backend:server")
val composeProject = project(":composeApp")
val sharedProject = project(":shared")
val serverSourceDir = serverProject.projectDir.resolve(jvmMainSourceDir).absolutePath
val serverBuildDir = serverProject.projectDir.resolve(jvmMainKspBuildMetaDataDir).absolutePath


val composeSourceDir = composeProject.projectDir.resolve(commonMainSourceDir).absolutePath
val composeBuildDir = composeProject.projectDir.resolve(commonMainKspBuildMetaDataDir).absolutePath

val sharedSourceDir = sharedProject.projectDir.resolve(commonMainSourceDir).absolutePath
val sharedBuildDir = sharedProject.projectDir.resolve(commonMainKspBuildMetaDataDir).absolutePath


plugins {
    id("com.google.devtools.ksp")
}

ksp {
    arg("serverSourceDir", serverSourceDir)
    arg("serverBuildDir", serverBuildDir)
    arg("composeSourceDir", composeSourceDir)
    arg("composeBuildDir", composeBuildDir)
    arg("sharedSourceDir", sharedSourceDir)
    arg("sharedBuildDir", sharedBuildDir)


}












