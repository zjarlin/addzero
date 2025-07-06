package com.addzero.kmp.exp

enum class ExceptionCode(value: Int) {
    JSON_NULL(10000)
}

class JsonException(private val status: String, private val desc: String) : Exception() {

    override val message: String
        get() = "[$status]${desc}"
}
