package com.addzero.model.entity

import com.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Table

/**
 * SysAiPrompt
 *
 * 对应数据库表: sys_ai_prompt
 * 自动生成的代码，请勿手动修改
 */
@Entity
@Table(name = "sys_ai_prompt")
interface SysAiPrompt : BaseEntity {

    /**
     * title
     */
    @Column(name = "title")
    val title: String

    /**
     * content
     */
    @Column(name = "content")
    val content: String

    /**
     * category
     */
    @Column(name = "category")
    val category: String

    /**
     * tags
     */
    @Column(name = "tags")
    val tags: String?

    /**
     * isBuiltIn
     */
    @Column(name = "is_built_in")
    val isBuiltIn: Boolean?
}