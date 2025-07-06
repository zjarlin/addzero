package com.addzero.web.infra.jimmer.base

import org.babyfish.jimmer.Formula
import org.babyfish.jimmer.sql.ManyToOne
import org.babyfish.jimmer.sql.OneToMany

//@MappedSuperclass
interface BaseTreeNode<T : BaseTreeNode<T>> {

    @Formula(dependencies = ["children"])
    val leafFlag: Boolean
        get() = children.isEmpty()


    @OneToMany(mappedBy = "parent")
    val children: List<T>

    @ManyToOne
    val parent: T?


}
