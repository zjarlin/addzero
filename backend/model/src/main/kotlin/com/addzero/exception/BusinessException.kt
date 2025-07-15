package com.addzero.exception

open class BusinessException : RuntimeException {

    private var errorCode: Int = -99999

    override val message: String
        get() = super.message ?: ""

    constructor(errorCode: ErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode.code
    }

    constructor(message: String) : super(message)

    constructor(cause: Throwable) : super(cause)

    constructor(
        message: String,
        cause: Throwable
    ) : super(message, cause)

    constructor(
        message: String,
        cause: Throwable,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super(message, cause, enableSuppression, writableStackTrace)
}