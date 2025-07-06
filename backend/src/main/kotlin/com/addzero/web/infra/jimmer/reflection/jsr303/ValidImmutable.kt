//package com.addzero.web.infra.jimmer.reflection.jsr303
//
//import jakarta.validation.*
//import org.babyfish.jimmer.ImmutableObjects
//import org.babyfish.jimmer.meta.ImmutableType
//import kotlin.reflect.KClass
//
//
//data class AnnotationInfo(
//    val annotationName: String, val annotationClass: Class<out Annotation>, val attributes: Map<String, Any?>
//)
//
//fun getValidationMetadata(entityClass: Class<*>): List<PropertyValidationMetadata> {
//    val immutableType = ImmutableType.get(entityClass)
//
//    val map = immutableType.declaredProps.map { prop ->
//
//
//        val immutableProp = prop.value
//        val annotationInfos = immutableProp.annotations.filter { annotation ->
//// 检查是否是JSR-303注解
//            annotation.annotationClass.java.getPackage().name.startsWith("javax.validation") || annotation.annotationClass.java.getPackage().name.startsWith("jakarta.validation")
//        }.map { annotation ->
//
//            val annotationClass = annotation.annotationClass.java
//            val attributes = mutableMapOf<String, Any?>()
//
//// 获取注解属性
//            annotationClass.declaredMethods.forEach { method ->
//                val value = method.invoke(annotation)
//                if (value != method.defaultValue) { // 只包含非默认值的属性
//                    attributes[method.name] = value
//                }
//            }
//
//            AnnotationInfo(
//                annotationClass.simpleName, annotationClass, attributes
//            )
//        }
//
//        PropertyValidationMetadata(
//            propertyName = immutableProp.name, annotations = annotationInfos
//        )
//    }
//    return map
//}
//
//data class PropertyValidationMetadata(
//    val propertyName: String, val annotations: List<AnnotationInfo>
//)
//
//
//@Target(AnnotationTarget.CLASS)
//@Retention(AnnotationRetention.RUNTIME)
//@Constraint(validatedBy = [ImmutableObjectValidator::class])
//annotation class ValidImmutable(
//    val message: String = "Validation failed", val groups: Array<KClass<*>> = [], val payload: Array<KClass<out Payload>> = []
//)
//
//class ImmutableObjectValidator : ConstraintValidator<ValidImmutable, Any> {
//    private lateinit var metadataProvider: (Class<*>) -> List<PropertyValidationMetadata>
//
//    override fun initialize(constraintAnnotation: ValidImmutable) {
//// 可以在这里初始化元数据提供器
//        metadataProvider = ::getValidationMetadata
//    }
//
//    override fun isValid(value: Any, context: ConstraintValidatorContext): Boolean {
//        val metadata = metadataProvider(value.javaClass)
//        var isValid = true
//
//// 创建临时Validator
//        val validator = Validation.buildDefaultValidatorFactory().validator
//
//        metadata.forEach { propMetadata ->
//            if (propMetadata.annotations.isNotEmpty()) {
//// 获取属性值
//                val propName = propMetadata.propertyName
//
//                val propValue = ImmutableObjects.get(value, propName)
//
//
//// 对每个注解进行验证
//                propMetadata.annotations.forEach { annotationInfo ->
//                    val violations = validator.validateValue(
//                        value.javaClass, propName, propValue, annotationInfo.annotationClass
//                    )
//
//                    if (violations.isNotEmpty()) {
//                        isValid = false
//                        violations.forEach { violation ->
//                            context.disableDefaultConstraintViolation()
//                            context.buildConstraintViolationWithTemplate(violation.messageTemplate).addPropertyNode(propName).addConstraintViolation()
//                        }
//                    }
//                }
//            }
//        }
//
//        return isValid
//    }
//
//}
//
//// 使用示例
//
