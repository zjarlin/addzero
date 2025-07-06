package com.addzero.web.modules.biz_dotfiles.entity

import com.addzero.kmp.generated.enums.EnumOsType
import com.addzero.kmp.generated.enums.EnumShellDefType
import com.addzero.kmp.generated.enums.EnumShellPlatforms
import com.addzero.kmp.generated.enums.EnumSysToggle
import com.addzero.kmp.generated.enums.EnumSysUserStatus
import com.addzero.web.infra.jimmer.base.baseentity.BaseEntity
import org.babyfish.jimmer.sql.Default
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.Table


/**
 * <p>
 *  环境变量管理

 * </p>
 *
 * @author zjarlin
 * @date 2024-10-20
 */
@Entity
@Table(name = "biz_dotfiles")

public interface BizDotfiles : BaseEntity {


    /**
     *  操作系统
     *  win=win
     * linux=linux
     * mac=mac
     *null=不限
     */

//    @Serialized
    val osType: List<EnumOsType>


//    @get:Schema(description = "操作系统")
//    @ManyToMany
//    @JoinTable(
//        name = "biz_mapping",
//        joinColumnName = "from_id",
//        inverseJoinColumnName = "to_id",
//        filter = JoinTableFilter(
//            columnName = "mapping_type",
//            values = ["dotfiles_tag_mapping"]
//        )
//    )
//    val osType: List<BizTag>



    /**
     *  系统架构
     *  arm64=arm64
     *  x86=x86
     *  不限=不限
     */
    @Key
    val osStructure: EnumShellPlatforms?

    /**
     *  定义类型
     *  alias=alias
     *  export=export
     * function=function
     * sh=sh
     * var=var
     */
    @Key
    val defType: EnumShellDefType

    /**
     *  名称
     */
    @Key
    val name: String

    /**
     *  值
     */
    val value: String

    /**
     *  注释
     */
    val describtion: String?

    /**
     *  状态
     *  1= 启用
     *  0= 未启用
     */
    @Key
    @Default("1")
    val status: EnumSysToggle

    /** 文件地址 */
    val fileUrl: String?


    /** 文件位置 */
    val location: String?

}
