plugins {
    id("site.addzero.buildlogic.kmp.cmp-lib")
}
kotlin{
   dependencies {
       implementation(projects.lib.shadcnComposeComponent)
   }
}
