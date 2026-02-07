plugins {
    id("site.addzero.buildlogic.kmp.composition.kmp-component")
}
kotlin{
   dependencies {
       implementation(projects.lib.shadcnComposeComponent)
   }
}
