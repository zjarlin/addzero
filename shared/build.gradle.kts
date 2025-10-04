//import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    id("kmp-lib")
    id("kmp-datetime")
    id("kmp-json")
    id("ksp4jdbc")
    id("ksp4enum")
    id("ksp4projectdir")
    id("kmp-config-link")
//    alias(libs.plugins.konfig)
}
dependencies {
    kspCommonMainMetadata("site.addzero:addzero-jdbc2enum-processor:${libs.versions.addzeroLTSVersion.get()}")
    //  kspCommonMainMetadata("site.addzero:addzero-enum-processor:+")
}
kotlin {
dependencies {
//            implementation(libs.ktorfit.lib)
            implementation(libs.kotlinx.datetime)
//            implementation("site.addzero:addzero-tool:${libs.versions.addzeroLTSVersion.get()}")
            implementation("site.addzero:addzero-compose-model-component:${libs.versions.addzeroLTSVersion.get()}")
            implementation("site.addzero:addzero-route-core:${libs.versions.addzeroLTSVersion.get()}")
            implementation("site.addzero:addzero-dict-trans-core:${libs.versions.addzeroLTSVersion.get()}")
            implementation("site.addzero:addzero-jimmer-model-lowquery:${libs.versions.addzeroLTSVersion.get()}")

            implementation("site.addzero:addzero-network-starter:${libs.versions.addzeroLTSVersion.get()}")
            implementation(libs.ktorfit.lib)

        }
    }
