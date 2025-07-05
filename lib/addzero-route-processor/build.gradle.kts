plugins {
    id("kmp-ksp")
}
kotlin{
   sourceSets{
      commonMain.dependencies {
         implementation(projects.lib.addzeroRouteCore) }
   }
}
