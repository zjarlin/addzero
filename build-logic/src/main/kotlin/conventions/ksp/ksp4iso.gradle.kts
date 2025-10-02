plugins {
    id("com.google.devtools.ksp")
}
ksp {
    arg("isomorphicPackageName", "site.addzero.generated.isomorphic")
    arg("iso2DataProviderPackage", "site.addzero.generated.forms.dataprovider")
    arg("formPackageName", "site.addzero.generated.forms")
    arg("isomorphicClassSuffix", "Iso")
}
