subprojects {
    if (
        name.contains("addzero")
    ) {
        apply(plugin = "publish-convention")
    }

}