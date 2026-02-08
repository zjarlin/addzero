
rootProject.name =rootDir.name
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
plugins {
//    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"

  id("site.addzero.gradle.plugin.addzero-git-dependency") version "+"
  id("site.addzero.gradle.plugin.modules-buddy") version "+"
  id("site.addzero.gradle.plugin.repo-buddy") version "+"
//    id("io.gitee.zjarlin.auto-modules") version "0.0.608"
}

