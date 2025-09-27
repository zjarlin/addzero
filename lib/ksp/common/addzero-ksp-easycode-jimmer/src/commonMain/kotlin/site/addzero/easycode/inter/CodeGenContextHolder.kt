package site.addzero.easycode.inter

object CodeGenContextHolder {
    lateinit var initContext: () -> Unit
     lateinit var absCodegenContextProvider:AbsCodegenContextProvider<*>
}
