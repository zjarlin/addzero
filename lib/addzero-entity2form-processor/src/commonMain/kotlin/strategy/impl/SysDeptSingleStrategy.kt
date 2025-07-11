package strategy.impl

import com.addzero.kmp.util.*
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * 🏢 SysDept 单选策略
 *
 * 专门处理 SysDept 类型字段，自动生成单选部门选择器
 *
 * 支持的字段模式：
 * - 类型为: SysDept (单选), List<SysDept> (多选), Set<SysDept> (多选)
 * - 不依赖字段名，只要类型匹配即可
 */
object SysDeptSingleStrategy : FormStrategy {

    override val priority: Int = 2 // 比 SysDeptsStrategy 更高的优先级

    override fun support(prop: KSPropertyDeclaration): Boolean {
        val typeName = prop.typeName
        // 只要类型包含 SysDept 就处理（不包含 SysDeptIso）
        return typeName.contains("SysDept")
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue
        val typeName = prop.typeName

        // 判断是否为多选模式
        return generateSingleSysDeptSelector(name, label, isRequired, defaultValue)
    }

    /**
     * 生成单选 SysDept 选择器 (SysDept)
     */
    private fun generateSingleSysDeptSelector(
        name: String,
        label: String,
        isRequired: Boolean,
        defaultValue: String
    ): String {
        return """
            AddSingleDeptSelector(
                selectedDept = state.value.$name,
                onValueChange = { selectedDept ->
                    state.value = state.value.copy($name = selectedDept)
                },
                allowClear = ${!isRequired}
            )
        """.trimIndent()
    }
}

/**
 * 🎯 使用示例
 *
 * 对于以下实体字段：
 *
 * ```kotlin
 * @Entity
 * interface Employee : BaseEntity {
 *     val department: SysDept?              // 所属部门
 *     val managedDepts: List<SysDept>       // 管理部门
 *     val parentDept: SysDept?              // 上级部门
 * }
 * ```
 *
 * 将自动生成对应的部门选择组件：
 *
 * 1. **department** -> AddSingleDeptSelector (单选部门选择器)
 * 2. **managedDepts** -> AddDeptSelector (多选部门选择器)
 * 3. **parentDept** -> AddSingleDeptSelector (单选部门选择器)
 *
 * 🔧 生成的代码将包含必要的导入：
 * - import com.addzero.kmp.component.form.AddDeptSelector (多选)
 * - import com.addzero.kmp.component.form.AddSingleDeptSelector (单选)
 * - import com.addzero.kmp.isomorphic.SysDeptIso
 * - import androidx.compose.runtime.*
 *
 * 📝 注意事项：
 *
 * 1. **类型转换**: SysDept 是 Jimmer 实体，需要转换为 SysDeptIso 用于 UI 显示
 * 2. **数据同步**: 需要实现 SysDeptIso 到 SysDept 的双向转换逻辑
 * 3. **查询优化**: 建议在转换时只查询必要的字段，避免性能问题
 * 4. **缓存策略**: 可以考虑缓存部门数据，减少重复查询
 *
 * 🔄 转换逻辑示例：
 *
 * ```kotlin
 * // SysDept -> SysDeptIso
 * fun SysDept.toIso(): SysDeptIso = SysDeptIso(
 *     id = this.id,
 *     name = this.name,
 *     children = this.children.map { it.toIso() }
 * )
 *
 * // SysDeptIso -> SysDept (需要查询数据库)
 * suspend fun SysDeptIso.toEntity(): SysDept? =
 *     sysDeptRepository.findById(this.id)
 * ```
 */
