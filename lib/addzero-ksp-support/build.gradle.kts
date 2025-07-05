import org.babyfish.jimmer.Versions

plugins {
    id("kmp-ksp")
}
kotlin{
   sourceSets{
      commonMain.dependencies {

      }
       jvmMain.dependencies {
           implementation("com.belerweb:pinyin4j:${Versions.pinyin4jVersion}")

       }
   }
}
