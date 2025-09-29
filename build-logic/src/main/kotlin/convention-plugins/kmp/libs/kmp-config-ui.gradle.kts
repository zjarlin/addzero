import site.addzero.gradle.tool.defByClass

plugins {
    id("com.codingfeline.buildkonfig")
}

buildkonfig {

       packageName = BuildSettings.PACKAGE_NAME

    defaultConfigs {
        defByClass(UiSettings::class)
    }

}
