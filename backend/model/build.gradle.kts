import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
//    id("kotlin-convention")
  id("site.addzero.buildlogic.jvm.kotlin-convention")
//    alias(libs.plugins.site)
//   site-addzero-jvm-kotlin-convention
  id("site.addzero.buildlogic.ksp.ksp4jdbc")
  id("site.addzero.buildlogic.ksp.ksp4iso")
  id("site.addzero.buildlogic.ksp.ksp4projectdir")
  id("site.addzero.buildlogic.ksp.ksp4autoddl")
}

val catalogLibs = extensions.getByType<VersionCatalogsExtension>().named("libs")
fun catalog(alias: String) = catalogLibs.findLibrary(alias).get()

dependencies {
  ksp(catalog("org-babyfish-jimmer-jimmer-ksp"))
//    ksp(projects.lib.ksp.common.addzeroKspEasycodeJimmer)
  ksp(catalog("site-addzero-jdbc2entity-processor"))
  ksp(catalog("site-addzero-entity2iso-processor"))
  ksp(catalog("site-addzero-entity2form-processor"))
  ksp(catalog("site-addzero-entity2mcp-processor"))
  implementation(catalog("site-addzero-entity2form-core"))



  implementation(catalog("org-babyfish-jimmer-jimmer-sql-kotlin"))
  //easy-excel
  implementation(catalog("cn-idev-excel-fastexcel"))
  //实体表单核心注解
  implementation(projects.shared)
  implementation(catalog("cn-hutool-hutool-all"))
}
ksp {
  arg("modelPackageName", "site.addzero.model.entity")
}
