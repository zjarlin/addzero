plugins {
    id("com.google.devtools.ksp")
}
ksp {
    arg("isomorphicPackageName", "com.addzero.kmp.generated.isomorphic")
    arg("iso2DataProviderPackage", "com.addzero.kmp.generated.forms.dataprovider")
    arg("formPackageName", "com.addzero.kmp.generated.forms")
    arg("isomorphicClassSuffix", "Iso")

}
