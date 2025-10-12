plugins {
    id("kmp-component")
}
kotlin{
   dependencies {
       implementation(projects.lib.shadcnComposeComponent)
   }
}
