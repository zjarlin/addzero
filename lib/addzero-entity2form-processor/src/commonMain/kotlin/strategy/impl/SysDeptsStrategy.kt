package strategy.impl

import com.addzero.kmp.util.*
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * 🏢 部门选择策略
 *
 * 自动识别部门相关字段并生成 AddDeptSelector 组件
 *
 * 支持的字段模式：
 * - 字段名包含: dept, department, 部门
 * - 类型为: SysDeptIso (单选), List<SysDeptIso> (多选), Set<SysDeptIso> (多选)
 */
object SysDeptsStrategy : FormStrategy {

    override val priority: Int = 3 // 高优先级，在基础类型策略之前

    override fun support(prop: KSPropertyDeclaration): Boolean {
        val ktName = prop.name.lowercase()
        val typeName = prop.typeName
        // 只处理部门对象类型，不处理ID类型
        // 1. 字段名模式匹配
        val nameMatches = ktName.equals("depts")
        return nameMatches
        // 2. 类型必须是 SysDeptIso 相关
//        val typeMatches = typeName.contains("SysDept")
//
//        val match = (typeName.contains("List") || typeName.contains("Set")) && typeMatches
//
//        // 只有当字段名匹配且类型是部门对象时才处理
//        return nameMatches && match
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue
        val typeName = prop.typeName

        // 判断是否为多选模式
//        val isMultiSelect = typeName.contains("List") || typeName.contains("Set")
        return generateMultiDeptSelector(name, label, isRequired, defaultValue)
    }

    /**
     * 生成多选部门选择器 (List<SysDeptIso>)
     */
    private fun generateMultiDeptSelector(
        name: String,
        label: String,
        isRequired: Boolean,
        defaultValue: String
    ): String {
        return """
            AddDeptSelector(
                selectedDepts = state.value.$name ?: emptyList(),
                onValueChange = { selectedDepts ->
                    state.value = state.value.copy($name = selectedDepts)
                },
            )
        """.trimIndent()
    }

    /**
     * 生成单选部门选择器 (SysDeptIso)
     */
    private fun generateSingleDeptSelector(
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
 * data class Employee(
 *     val department: SysDeptIso?,          // 单个部门对象
 *     val departments: List<SysDeptIso>,    // 多个部门对象
 *     val managedDepts: Set<SysDeptIso>     // 管理的部门集合
 * )
 * ```
 *
 * 将自动生成对应的部门选择组件：
 *
 * 1. **department** -> AddSingleDeptSelector (单选部门选择器)
 * 2. **departments** -> AddDeptSelector (多选部门选择器)
 * 3. **managedDepts** -> AddDeptSelector (多选部门选择器)
 *
 * 🔧 生成的代码将包含必要的导入：
 * - import com.addzero.kmp.component.form.AddDeptSelector (多选)
 * - import com.addzero.kmp.component.form.AddSingleDeptSelector (单选)
 * - import com.addzero.kmp.isomorphic.SysDeptIso
 * - import androidx.compose.runtime.*
 */
