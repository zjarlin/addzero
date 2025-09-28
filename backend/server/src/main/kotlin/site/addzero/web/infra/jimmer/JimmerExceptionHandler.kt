package site.addzero.web.infra.jimmer

import org.babyfish.jimmer.UnloadedException
import org.babyfish.jimmer.error.CodeBasedException
import org.babyfish.jimmer.error.CodeBasedRuntimeException
import org.babyfish.jimmer.jackson.ImmutableModuleRequiredException
import org.babyfish.jimmer.sql.exception.CircularDeletionException
import org.babyfish.jimmer.sql.exception.DatabaseValidationException
import org.babyfish.jimmer.sql.exception.EmptyResultException
import org.babyfish.jimmer.sql.exception.TooManyResultsException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException
import site.addzero.web.infra.config.log
import java.net.URI

@RestControllerAdvice
@org.springframework.core.annotation.Order(0)
class JimmerExceptionHandler {
    private fun createBaseProblemDetail(
        status: HttpStatus,
        detail: String?,
        typeUri: String?
    ) = ProblemDetail.forStatusAndDetail(status, detail).apply {
        type = URI.create("$PROBLEM_TYPE_URN_PREFIX$typeUri")
    }

    private fun createStandardProblemDetail(
        status: HttpStatus,
        detail: String?,
        type: String?,
        title: String?,
        errorCode: String?,
        message: String? = null
    ) = createBaseProblemDetail(status, detail, type).apply {
        this.title = title
        setProperty(ERROR_CODE_PROPERTY, errorCode)
        message?.let { setProperty(MESSAGE_PROPERTY, it) }
    }

    private fun createCodeBasedProblemDetail(
        status: HttpStatus,
        family: String?,
        code: String?,
        detail: String?,
        fields: Map<String, Any?>,
        message: String?
    ) = createBaseProblemDetail(status, detail, "$family:$code").apply {
        this.title = family
        setProperty(FAMILY_PROPERTY, family)
        setProperty(CODE_PROPERTY, code)
        message?.let { setProperty(MESSAGE_PROPERTY, it) }
        fields.forEach { (name, value) ->
            setProperty(name, value)
        }
    }

    @ExceptionHandler(CodeBasedException::class)
    fun handleCodeBasedException(ex: CodeBasedException) = createCodeBasedProblemDetail(
        HttpStatus.BAD_REQUEST,
        ex.family,
        ex.code,
        ex.localizedMessage,
        ex.fields,
        ex.message
    ).also { log.error("CodeBasedException: {}", ex.message, ex) }

    @ExceptionHandler(CodeBasedRuntimeException::class)
    fun handleCodeBasedRuntimeException(ex: CodeBasedRuntimeException) = createCodeBasedProblemDetail(
        HttpStatus.BAD_REQUEST,
        ex.family,
        ex.code,
        ex.localizedMessage,
        ex.fields,
        ex.message
    ).also { log.error("CodeBasedRuntimeException: {}", ex.message, ex) }

    @ExceptionHandler(org.babyfish.jimmer.CircularReferenceException::class)
    fun handleCircularReferenceException(ex: org.babyfish.jimmer.CircularReferenceException) =
        createStandardProblemDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "数据关联错误：检测到循环引用，请检查实体间的双向关联配置",
            JIMMER_CIRCULAR_REFERENCE_TYPE,
            "循环引用错误",
            CIRCULAR_REFERENCE_ERROR_CODE,
            ex.message
        ).also { log.error("CircularReferenceException: {}", ex.message, ex) }

    @ExceptionHandler(CircularDeletionException::class)
    fun handleCircularDeletionException(ex: CircularDeletionException) = createStandardProblemDetail(
        HttpStatus.CONFLICT,
        "删除操作失败：检测到循环删除依赖，无法完成删除操作",
        JIMMER_CIRCULAR_DELETION_TYPE,
        "循环删除错误",
        CIRCULAR_DELETION_ERROR_CODE,
        ex.message
    ).also { log.error("CircularDeletionException: {}", ex.message, ex) }

    @ExceptionHandler(DatabaseValidationException::class)
    fun handleDatabaseValidationException(ex: DatabaseValidationException) = createStandardProblemDetail(
        HttpStatus.BAD_REQUEST,
        "数据库验证失败：${ex.message}",
        JIMMER_DATABASE_VALIDATION_TYPE,
        "数据库验证错误",
        DATABASE_VALIDATION_FAILED_ERROR_CODE
    ).also { log.error("DatabaseValidationException: {}", ex.message, ex) }

    @ExceptionHandler(org.babyfish.jimmer.sql.exception.ExecutionException::class)
    fun handleExecutionException(ex: org.babyfish.jimmer.sql.exception.ExecutionException) =
        createStandardProblemDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "数据库操作失败：SQL执行出错",
            JIMMER_SQL_EXECUTION_TYPE,
            "SQL执行错误",
            SQL_EXECUTION_ERROR_CODE,
            ex.message
        ).also { log.error("ExecutionException: {}", ex.message, ex) }

    @ExceptionHandler(EmptyResultException::class)
    fun handleEmptyResultException(ex: EmptyResultException) = createStandardProblemDetail(
        HttpStatus.NOT_FOUND,
        "查询结果为空：期望返回至少一条记录，但查询结果为空",
        JIMMER_EMPTY_RESULT_TYPE,
        "查询结果为空",
        EMPTY_RESULT_ERROR_CODE,
        ex.message
    ).also { log.warn("EmptyResultException: {}", ex.message) }

    @ExceptionHandler(TooManyResultsException::class)
    fun handleTooManyResultsException(ex: TooManyResultsException) = createStandardProblemDetail(
        HttpStatus.CONFLICT,
        "查询结果过多：期望返回单条记录，但查询返回了多条记录",
        JIMMER_TOO_MANY_RESULTS_TYPE,
        "查询结果过多",
        TOO_MANY_RESULTS_ERROR_CODE,
        ex.message
    ).also { log.warn("TooManyResultsException: {}", ex.message) }

    @ExceptionHandler(org.babyfish.jimmer.sql.exception.SerializationException::class)
    fun handleSerializationException(ex: org.babyfish.jimmer.sql.exception.SerializationException) =
        createStandardProblemDetail(
            HttpStatus.BAD_REQUEST,
            "序列化失败：数据序列化或反序列化过程中出错",
            JIMMER_SERIALIZATION_TYPE,
            "序列化错误",
            SERIALIZATION_ERROR_CODE,
            ex.message
        ).also { log.error("SerializationException: {}", ex.message, ex) }

    @ExceptionHandler(ImmutableModuleRequiredException::class)
    fun handleImmutableModuleRequiredException(ex: ImmutableModuleRequiredException) = createStandardProblemDetail(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "配置错误：Jackson缺少Jimmer不可变对象模块，请检查Jackson配置",
        JIMMER_JACKSON_MODULE_MISSING_TYPE,
        "Jackson模块缺失",
        JACKSON_MODULE_MISSING_ERROR_CODE,
        ex.message
    ).also { log.error("ImmutableModuleRequiredException: {}", ex.message, ex) }

    @ExceptionHandler(UnloadedException::class)
    fun handleUnloadedException(ex: UnloadedException): ProblemDetail {
        log.warn("UnloadedException: {}", ex.message)
        val className = ex.type.simpleName
        val propertyName = ex.prop
        val errorDetail = "数据访问错误：实体 '$className' 的属性 '$propertyName' 未加载，请确保在查询时包含此属性"

        return createStandardProblemDetail(
            HttpStatus.BAD_REQUEST,
            errorDetail,
            JIMMER_UNLOADED_PROPERTY_TYPE,
            "属性未加载错误",
            UNLOADED_PROPERTY_ERROR_CODE,
            ex.message
        ).apply {
            setProperty(ENTITY_TYPE_PROPERTY, className)
            setProperty(PROPERTY_NAME_PROPERTY, propertyName)
        }
    }

    @ExceptionHandler(HandlerMethodValidationException::class)
    fun handleHandlerMethodValidationException(ex: HandlerMethodValidationException): ProblemDetail {
        log.warn("HandlerMethodValidationException: {}", ex.message)

        val errorMessages = ex.allValidationResults
            .flatMap { it.resolvableErrors.orEmpty() }
            .map { it.defaultMessage ?: DEFAULT_VALIDATION_ERROR_MESSAGE }
            .joinToString(ERROR_MESSAGE_SEPARATOR)

        return createStandardProblemDetail(
            HttpStatus.BAD_REQUEST,
            "参数验证失败：$errorMessages",
            VALIDATION_METHOD_ARGUMENT_TYPE,
            "方法参数验证失败",
            METHOD_VALIDATION_FAILED_ERROR_CODE,
            ex.message
        ).apply {
            setProperty(VALIDATION_ERRORS_PROPERTY, errorMessages)
        }
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        ex: org.springframework.web.bind.MethodArgumentNotValidException
    ): ProblemDetail {
        log.warn("MethodArgumentNotValidException: {}", ex.message)

        val errorMessages = ex.bindingResult.fieldErrors
            .map { it.defaultMessage ?: DEFAULT_VALIDATION_ERROR_MESSAGE }
            .joinToString(ERROR_MESSAGE_SEPARATOR)

        return createStandardProblemDetail(
            HttpStatus.BAD_REQUEST,
            "参数验证失败：$errorMessages",
            VALIDATION_REQUEST_BODY_TYPE,
            "请求体验证失败",
            REQUEST_VALIDATION_FAILED_ERROR_CODE,
            ex.message
        ).apply {
            setProperty(VALIDATION_ERRORS_PROPERTY, errorMessages)
        }
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(ex: AccessDeniedException) = createStandardProblemDetail(
        HttpStatus.FORBIDDEN,
        "访问被拒绝：权限不足",
        SECURITY_ACCESS_DENIED_TYPE,
        "访问被拒绝",
        ACCESS_DENIED_ERROR_CODE
    ).also { log.warn("AccessDeniedException: {}", ex.message) }

    companion object {
        private const val MESSAGE_PROPERTY = "message"
        private const val FAMILY_PROPERTY = "family"
        private const val CODE_PROPERTY = "code"
        private const val ERROR_CODE_PROPERTY = "errorCode"
        private const val ENTITY_TYPE_PROPERTY = "entityType"
        private const val PROPERTY_NAME_PROPERTY = "propertyName"
        private const val VALIDATION_ERRORS_PROPERTY = "validationErrors"
        private const val PROBLEM_TYPE_URN_PREFIX = "urn:problem-type:"
        private const val ERROR_MESSAGE_SEPARATOR = "；"
        private const val DEFAULT_VALIDATION_ERROR_MESSAGE = "验证失败"

        private const val JIMMER_CIRCULAR_REFERENCE_TYPE = "jimmer:circular-reference"
        private const val JIMMER_CIRCULAR_DELETION_TYPE = "jimmer:circular-deletion"
        private const val JIMMER_DATABASE_VALIDATION_TYPE = "jimmer:database-validation"
        private const val JIMMER_SQL_EXECUTION_TYPE = "jimmer:sql-execution"
        private const val JIMMER_EMPTY_RESULT_TYPE = "jimmer:empty-result"
        private const val JIMMER_TOO_MANY_RESULTS_TYPE = "jimmer:too-many-results"
        private const val JIMMER_SERIALIZATION_TYPE = "jimmer:serialization"
        private const val JIMMER_JACKSON_MODULE_MISSING_TYPE = "jimmer:jackson-module-missing"
        private const val JIMMER_UNLOADED_PROPERTY_TYPE = "jimmer:unloaded-property"
        private const val VALIDATION_METHOD_ARGUMENT_TYPE = "validation:method-argument"
        private const val VALIDATION_REQUEST_BODY_TYPE = "validation:request-body"
        private const val SECURITY_REQUEST_REJECTED_TYPE = "security:request-rejected"
        private const val SECURITY_ACCESS_DENIED_TYPE = "security:access-denied"

        private const val CIRCULAR_REFERENCE_ERROR_CODE = "CIRCULAR_REFERENCE"
        private const val CIRCULAR_DELETION_ERROR_CODE = "CIRCULAR_DELETION"
        private const val DATABASE_VALIDATION_FAILED_ERROR_CODE = "DATABASE_VALIDATION_FAILED"
        private const val SQL_EXECUTION_ERROR_CODE = "SQL_EXECUTION_ERROR"
        private const val EMPTY_RESULT_ERROR_CODE = "EMPTY_RESULT"
        private const val TOO_MANY_RESULTS_ERROR_CODE = "TOO_MANY_RESULTS"
        private const val SERIALIZATION_ERROR_CODE = "SERIALIZATION_ERROR"
        private const val JACKSON_MODULE_MISSING_ERROR_CODE = "JACKSON_MODULE_MISSING"
        private const val UNLOADED_PROPERTY_ERROR_CODE = "UNLOADED_PROPERTY"
        private const val METHOD_VALIDATION_FAILED_ERROR_CODE = "METHOD_VALIDATION_FAILED"
        private const val REQUEST_VALIDATION_FAILED_ERROR_CODE = "REQUEST_VALIDATION_FAILED"
        private const val REQUEST_REJECTED_ERROR_CODE = "REQUEST_REJECTED"
        private const val ACCESS_DENIED_ERROR_CODE = "ACCESS_DENIED"
    }
}
