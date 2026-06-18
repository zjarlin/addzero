//import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
  id("site.addzero.buildlogic.kmp.cmp-lib")
  id("site.addzero.buildlogic.kmp.kmp-datetime")
  id("site.addzero.buildlogic.kmp.kmp-json")
  id("site.addzero.buildlogic.ksp.ksp4jdbc")
  id("site.addzero.buildlogic.ksp.ksp4enum")
  id("site.addzero.buildlogic.ksp.ksp4projectdir")
  id("site.addzero.buildlogic.kmp.kmp-konfig")
}

val catalogLibs = extensions.getByType<VersionCatalogsExtension>().named("libs")
fun catalog(alias: String) = catalogLibs.findLibrary(alias).get()

dependencies {
  kspCommonMainMetadata("site.addzero:jdbc2enum-processor:${libs.versions.addzeroLTSVersion.get()}")
  //  kspCommonMainMetadata("site.addzero:enum-processor:+")
}
kotlin {
  dependencies {
//            implementation(libs.ktorfit.lib)
    implementation(catalog("org-jetbrains-kotlinx-kotlinx-datetime"))
//            implementation("site.addzero:tool:${libs.versions.addzeroLTSVersion.get()}")
    implementation(catalog("site-addzero-compose-model-component"))
    implementation(catalog("site-addzero-route-core"))
    implementation(catalog("site-addzero-dict-trans-core"))
    implementation(catalog("site-addzero-jimmer-model-lowquery-v2025"))
    implementation(catalog("site-addzero-network-starter"))
    implementation(catalog("de-jensklingenberg-ktorfit-ktorfit-lib"))

  }
}
