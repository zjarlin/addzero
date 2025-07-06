//import com.addzero.web.infra.jimmer.enum.BaseEnum
//import com.fasterxml.jackson.core.JsonParser
//import com.fasterxml.jackson.core.JsonToken
//import com.fasterxml.jackson.databind.DeserializationContext
//import com.fasterxml.jackson.databind.JavaType
//import com.fasterxml.jackson.databind.JsonDeserializer
//
//class BasicEnumDeserializer<T : Enum<T>, V>(val jdbcType: JavaType) : JsonDeserializer<BaseEnum<T, V>>() {
//    override fun deserialize(
//        p: JsonParser,
//        ctx: DeserializationContext,
//    ): BaseEnum<T, V>? {
//        val vallist = jdbcType.rawClass.enumConstants
//        if (p.hasToken(JsonToken.VALUE_STRING)) {
//            val columnValue = p.text
//            val filter = vallist.filter { it is BaseEnum<*, *> && it is Enum<*> }
//                .map { it as BaseEnum<T, V> }.firstOrNull { it.columnValue == columnValue }
//            return filter
//        }
//        if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
//            val columnValue = p.intValue
//            val filter = vallist.filter { it is BaseEnum<*, *> && it is Enum<*> }
//                .map { it as BaseEnum<T, V> }.firstOrNull { it.columnValue == columnValue }
//            return filter
//        }
//        return null
//    }
//
//}
