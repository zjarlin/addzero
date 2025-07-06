package com.addzero.addzero_common

import com.addzero.kmp.generated.enums.EnumOsType
import com.addzero.kmp.generated.enums.EnumShellDefType
import com.addzero.kmp.generated.enums.EnumShellPlatforms
import com.addzero.kmp.generated.enums.EnumSysToggle
import com.addzero.kmp.generated.enums.EnumSysUserStatus
import com.addzero.web.infra.jimmer.`enumValueIn?`
import com.addzero.web.modules.biz_dotfiles.entity.BizDotfiles
import com.addzero.web.modules.biz_dotfiles.entity.osType
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor


@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class 测试枚举插入(
    val sql: KSqlClient,
) {
    @Test
    fun 测试枚举插入(): Unit {
        val bizDotfiles = BizDotfiles {
            osType = listOf(EnumOsType.MAC, EnumOsType.LINUX) //保存MAC和LINUX
            osStructure = EnumShellPlatforms.ARM64
            defType = EnumShellDefType.FUNCTION
            name = "visual-studio-code"
            value = "oxxxxxxxxxxxx"
            describtion = "vscode编辑器"
            status = EnumSysToggle.KAIQI
            fileUrl = null
            location = "/Applications/Visual Studio Code.app"
        }
//        val save = sql.save(bizDotfiles)
        println()
    }


    @Test
    fun `测试ListEnum查询`() {
        val execute = sql.createQuery(BizDotfiles::class) {
            val value = listOf(EnumOsType.WIN, EnumOsType.MAC) //期望能查出来
//            val value1 = listOf(EnumOsType.WIN)//期望查不出,因为没存WIN
            val predicates = table.osType `enumValueIn?` value
            where(predicates)
            select(table)
        }.execute()
        println()
    }


}
