package site.addzero.web.infra.jimmer.base

import cn.hutool.core.util.TypeUtil
import kotlin.jvm.kotlin
import kotlin.reflect.KClass

interface BaseGenericContext<E:Any> {

       fun CLASS(): Class<E> {
        val typeArgument = TypeUtil.getTypeArgument(this.javaClass, 0)
        val type = typeArgument as Class<E>
        return type
    }


}
