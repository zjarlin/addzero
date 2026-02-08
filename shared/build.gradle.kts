//import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
  id("site.addzero.buildlogic.kmp.cmp-lib")
  id("site.addzero.buildlogic.kmp.kmp-datetime")
  id("site.addzero.buildlogic.kmp.kmp-json")
  id("site.addzero.buildlogic.ksp.ksp4jdbc")
  id("site.addzero.buildlogic.ksp.ksp4enum")
  id("site.addzero.buildlogic.ksp.ksp4projectdir")
  id("site.addzero.buildlogic.kmp.kmp-konfig")
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
    implementation(libs.addzero.compose.model.component)
    implementation(libs.route.core)
    implementation(libs.dict.trans.core)
    implementation(libs.jimmer.model.lowquery)
    implementation(libs.network.starter)
    implementation(libs.ktorfit.lib)

  }
}
