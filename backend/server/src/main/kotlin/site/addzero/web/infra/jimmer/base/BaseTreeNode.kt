package site.addzero.web.infra.jimmer.base

import org.babyfish.jimmer.Formula
import org.babyfish.jimmer.sql.DissociateAction
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.ManyToOne
import org.babyfish.jimmer.sql.OnDissociate
import org.babyfish.jimmer.sql.OneToMany

//@MappedSuperclass
interface BaseTreeNode<T : BaseTreeNode<T>> {

    @Formula(dependencies = ["children"])
    val leafFlag: Boolean
        get() = children.isEmpty()


    @OneToMany(mappedBy = "parent")
    val children: List<T>

    @ManyToOne
          @OnDissociate(DissociateAction.DELETE)

    val parent: T?


}
