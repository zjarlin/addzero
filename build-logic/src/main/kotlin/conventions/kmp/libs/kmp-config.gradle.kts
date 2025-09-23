import com.codingfeline.buildkonfig.compiler.FieldSpec
import site.addzero.gradle.tool.KonfigUtil.getProjectProperties
import site.addzero.gradle.tool.defByClass
import site.addzero.gradle.tool.defByMap

plugins {
    id("com.codingfeline.buildkonfig")
}

buildkonfig {
    packageName = BuildSettings.PACKAGE_NAME
    defaultConfigs {
        defByClass(BuildSettings::class)
    }
    defaultConfigs {
        val projectProperties = getProjectProperties(project,".env.prod")
        defByMap(projectProperties)
    }

    defaultConfigs("dev") {
        buildConfigField(FieldSpec.Type.STRING, "dasda", "sss")
    }


}
