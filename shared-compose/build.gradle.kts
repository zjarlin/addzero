plugins {
  id("site.addzero.buildlogic.kmp.cmp-lib")
  id("site.addzero.buildlogic.kmp.kmp-ksp-plugin")
    id("site.addzero.buildlogic.kmp.kmp-ktorfit")
//  id("site.addzero.buildlogic.conventions.addzero-component")
  id("site.addzero.buildlogic.kmp.kmp-json-withtool")
  id("site.addzero.buildlogic.kmp.kmp-datetime")
}
dependencies {
  kspCommonMainMetadata(libs.apiprovider.processor)
}

kotlin {
  dependencies {
//    implementation(libs.bundles.addzero.component)
    implementation(libs.addzero.tool)
    implementation(libs.jimmer.model.lowquery)
    implementation(libs.network.starter)

    implementation(projects.shared)
  }
}
