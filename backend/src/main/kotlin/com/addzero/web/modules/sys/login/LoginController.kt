package com.addzero.web.modules.sys.login

import cn.dev33.satoken.stp.StpUtil
import com.addzero.common.consts.sql
import com.addzero.kmp.entity.SecondLoginDTO
import com.addzero.kmp.entity.SecondLoginResponse
import com.addzero.kmp.entity.SignInStatus
import com.addzero.kmp.exp.BizException
import com.addzero.kmp.generated.isomorphic.SysUserIso
import com.addzero.web.infra.jackson.convertTo
import com.addzero.web.infra.jimmer.toJimmerEntity
import com.addzero.web.modules.sys_user.controller.SysUserService
import com.addzero.web.modules.sys_user.entity.SysUser
import com.addzero.web.modules.sys_user.entity.email
import com.addzero.web.modules.sys_user.entity.phone
import com.addzero.web.modules.sys_user.entity.username
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.or
import org.springframework.web.bind.annotation.*

@RequestMapping("/sys/login")
@RestController
class LoginController(private val sysUserService: SysUserService)  {

    @GetMapping("/hasPermition")
     fun hasPermition(@RequestParam code: String): Boolean {
        return true
    }

    @PostMapping("/signin")
     fun signin(@RequestBody loginRe: String): SignInStatus {

        val executeQuery = sql.executeQuery(SysUser::class) {
            where(
                or(
                    table.username eq loginRe,
                    table.email eq loginRe,
                    table.phone eq loginRe,
                )
            )
            select(table)
        }.firstOrNull()
        val checkSignInput = identifyInputType(loginRe)

        if (executeQuery == null) {
            val notregister = SignInStatus.Notregister(checkSignInput)
            return notregister
        }

        val sysUserIso = executeQuery.convertTo<SysUserIso>()
        val alredyregister = SignInStatus.Alredyregister(checkSignInput, sysUserIso)
        return alredyregister
    }
//    fun

    /**
     * 注册一个用户
     * @param [userRegFormState]
     * @return [Boolean]
     */
    @PostMapping("/signup")
     fun signup(@RequestBody userRegFormState: SysUserIso): Boolean {
        val toJimmerEntity = userRegFormState.toJimmerEntity<SysUserIso, SysUser>()

        val save = sql.save(toJimmerEntity)
        val rowAffected = save.isRowAffected
        val modified = save.isModified

        return rowAffected
//        log.info(save.modifiedEntity)
    }

    @PostMapping("/signinSecond")
     fun signinSecond(@RequestBody secondLoginDTO: SecondLoginDTO): SecondLoginResponse {
        val findByUserRegFormState = sysUserService.findByUserRegFormState(secondLoginDTO) ?: throw BizException("用户不存在")
        val password = secondLoginDTO.userRegFormState.password
        if (findByUserRegFormState.password != password) {
            throw BizException("密码错误")
        }
        StpUtil.login(findByUserRegFormState.id)
        val tokenInfo = StpUtil.getTokenInfo()
        val tokenValue = tokenInfo.tokenValue
        val secondLoginResponse = SecondLoginResponse(findByUserRegFormState.convertTo(), tokenValue)
        return secondLoginResponse

    }

}

