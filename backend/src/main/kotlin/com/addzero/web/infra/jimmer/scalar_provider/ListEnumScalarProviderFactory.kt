package com.addzero.web.infra.jimmer.scalar_provider

import com.addzero.web.infra.jimmer.enum.BaseEnum
import org.babyfish.jimmer.meta.ImmutableProp
import org.babyfish.jimmer.sql.runtime.PropScalarProviderFactory
import org.babyfish.jimmer.sql.runtime.ScalarProvider
import org.springframework.stereotype.Component

@Component
class ListEnumScalarProviderFactory() : PropScalarProviderFactory {
    override fun createScalarProvider(prop: ImmutableProp): ScalarProvider<*, *>? {
        val elementClass = prop.elementClass
        val bool = elementClass.isEnum && BaseEnum::class.java.isAssignableFrom(elementClass)
        if (bool) {
            return GenericEnumScalarProvider(elementClass)
        }
        return null
    }
}
