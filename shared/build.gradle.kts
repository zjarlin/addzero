//import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    id("kmp-lib")
    id("kmp-datetime")
    id("kmp-json")
    id("ksp4jdbc")
    id("ksp4enum")
    id("ksp4projectdir")
//    id("kmp-config")
//    alias(libs.plugins.konfig)

}




dependencies {
    kspCommonMainMetadata("site.addzero:addzero-jdbc2enum-processor:+")
  //  kspCommonMainMetadata("site.addzero:addzero-enum-processor:+")
}


kotlin {
    sourceSets {
        commonMain.dependencies {
//            implementation(libs.ktorfit.lib)

            implementation(libs.kotlinx.datetime)

            implementation("site.addzero:addzero-tool:+")
                implementation("site.addzero:addzero-compose-model-component:+")
    implementation("site.addzero:addzero-route-core:+")
    implementation("site.addzero:addzero-dict-trans-core:+")
    implementation("site.addzero:addzero-jimmer-model-lowquery:+")

    implementation("site.addzero:addzero-network-starter:+")

            implementation(libs.ktorfit.lib)

        }
    }
}
