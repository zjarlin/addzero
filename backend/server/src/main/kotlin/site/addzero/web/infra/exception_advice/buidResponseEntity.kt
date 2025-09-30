package site.addzero.web.infra.exception_advice

inline fun <reified E : Throwable> E?.buildMessage(): String {
    return buildString {
        var currentCause: Throwable? = this@buildMessage
        while (currentCause != null) {
            append(currentCause.message ?: currentCause.toString())
            currentCause = currentCause.cause
            if (currentCause != null) {
                append(System.lineSeparator())
                append("Caused by: ")
            }
        }
    }
}
