package com.addzero.web.infra.jimmer.base

interface SelectOption<V> {
    val label: String

    val value: V
}

data class SelectOptionImpl<V>(
    override val label: String,
    override val value: V,
) : SelectOption<V>
