package com.addzero.ai.mcp

import com.addzero.common.consts.sql
import com.addzero.kmp.generated.isomorphic.SysDictIso
import com.addzero.web.infra.jackson.toJson
import com.addzero.web.modules.sys_dict.entity.SysDict
import org.babyfish.jimmer.ImmutableObjects
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

//interface AiCrudService<Iso : Any, E : Any> {
//    val des: String
//    fun CLASS_ISO(): KClass<Iso> {
//        val typeArgument = TypeUtil.getTypeArgument(this.javaClass, 0)
//        val type = typeArgument as Class<Iso>
//        return type.kotlin
//    }
//
//    fun CLASS_E(): KClass<E> {
//        val typeArgument = TypeUtil.getTypeArgument(this.javaClass, 1)
//        val type = typeArgument as Class<E>
//        return type.kotlin
//    }
//
//    fun save(entity: Iso): E {
//        val toJson = entity.toJson()
//        val fromString = ImmutableObjects.fromString(CLASS_E().java, toJson)
//        val save = sql.save(fromString)
//        val modifiedEntity = save.modifiedEntity
//        return modifiedEntity
//    }
//
//    @Bean
//    fun aiSaveToolCallback(): MethodToolCallback {
//        val isoClass = CLASS_ISO().java
//        val method = ReflectUtil.getMethod(this.javaClass, "save", isoClass)
//        val toolCallback = MethodToolCallback.builder()
//            .toolDefinition(
//                ToolDefinition.builder()
//                    .name("保存$des")
//                    .description("保存数据库${des}实体")
//                    .inputSchema(JsonSchemaGenerator.generateForMethodInput(method))
//                    .build()
//            )
//            .toolMethod(method)
//            .toolObject(this)
//            .build()
//        return toolCallback
//    }
//
//
//}

//@Service
//class DictService : AiCrudService<SysDictIso, SysDict> {
//    override val des: String
//        get() = "字典"
//
//}


@Service
open class DictService {
    @Tool(description = "保存字典")
    fun save(entity: SysDictIso): String {
        val toJson = entity.toJson()
        val fromString = ImmutableObjects.fromString(SysDict::class.java, toJson)
        val save = sql.save(entity)
        val modifiedEntity = save.modifiedEntity
        return "保存成功"
//        return modifiedEntity
//        return modifiedEntity
    }
}

//其他泛型的实体service
