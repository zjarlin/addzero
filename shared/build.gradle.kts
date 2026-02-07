//import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    id("site.addzero.buildlogic.kmp.composition.kmp-lib")
    id("site.addzero.buildlogic.kmp.libs.kmp-datetime")
    id("site.addzero.buildlogic.kmp.libs.kmp-json")
    id("site.addzero.buildlogic.ksp.ksp4jdbc")
    id("site.addzero.buildlogic.ksp.ksp4enum")
    id("site.addzero.buildlogic.ksp.ksp4projectdir")
//    id("kmp-config-link")
//    alias(libs.plugins.konfig)
}
dependencies {
    kspCommonMainMetadata("site.addzero:jdbc2enum-processor:${libs.versions.addzeroLTSVersion.get()}")
    //  kspCommonMainMetadata("site.addzero:enum-processor:+")
}
kotlin {
dependencies {
//            implementation(libs.ktorfit.lib)
            implementation(libs.kotlinx.datetime)
//            implementation("site.addzero:tool:${libs.versions.addzeroLTSVersion.get()}")
            implementation(libs.compose.model.component)
            implementation(libs.route.core)
            implementation(libs.dict.trans.core)
            implementation(libs.jimmer.model.lowquery)
            implementation(libs.network.starter)
            implementation(libs.ktorfit.lib)

        }
    }
