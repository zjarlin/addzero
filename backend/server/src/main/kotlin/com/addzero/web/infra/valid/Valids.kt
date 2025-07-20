package com.addzero.web.infra.valid

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.text.CharSequenceUtil
import cn.hutool.core.util.ReflectUtil
import cn.hutool.core.util.StrUtil
import com.addzero.common.util.metainfo.MetaInfoUtils
import com.addzero.kmp.entity.Res
import com.addzero.kmp.kt_util.addAll
import com.addzero.web.infra.valid.entity.Des
import com.addzero.web.infra.valid.entity.ValidVO
import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import java.util.function.Consumer

/**
 * valid校验封装
 *
 * @author zjarlin
 * @since 2022/07/19
 */
object Valids {
    /**
     * @param list
     * @param consumer 批量导入方法
     * @return [Result]<[ValidVO]>
     * @author zjarlin
     * @since 2023/10/07
     */
    inline fun <reified T> returnValidList(list: List<T>, consumer: Consumer<List<T>>): Res<*> {
        if (CollUtil.isEmpty(list)) {
            val fail = Res.fail("导入数据不能为空")
            return fail
        }
        val tClass = T::class.java
        val validate: List<JSONObject> = validate(list)

//        val all: Map<Boolean, List<JSONObject>> =
        val all = validate.partition {
            val msg = it.getString("msg") // 使用 getString 获取
            CharSequenceUtil.isBlank(msg)
        }
        val successList = all.first
        val errorList = all.second

        if (CollUtil.isNotEmpty(successList)) {
            val collect = successList.map { jsonObject ->
                JSON.parseObject(jsonObject.toJSONString(), tClass)
            }
            consumer.accept(collect)
        }

        val objectValidList = ValidVO().apply {
            errorNo = errorList.size
            successNo = successList.size
            this.successList = successList
            this.errorList = errorList
        }

        return if (objectValidList.errorNo!! > 0) {
            Res.success("失败条数${objectValidList.errorNo}，成功条数${objectValidList.successNo}", objectValidList)
        } else {
            Res.success("全部导入成功!成功条数${objectValidList.successNo}")
        }
    }

    /**
     * 校验list增加一列报错信息
     *
     * @param list 列表 入参
     * @return [List]<[JSONObject]>
     * @author zjarlin
     * @since 2023/04/15
     */
    fun <T> validate(list: List<T>): List<JSONObject> {
        if (CollUtil.isEmpty(list)) {
            return emptyList()
        }

        return list.map { item ->
            val jsonObject = JSON.parseObject(JSON.toJSONString(item))
            val constraintViolations: List<Des> = getValidDes(item)
            val msg = constraintViolations.joinToString(System.lineSeparator()) { des ->
                val fieldComment: String = des.fieldComment
                val message: String = des.message
                StrUtil.addPrefixIfNot(message, fieldComment)
            }
            val describe = constraintViolations.groupBy { it.fieldName }
            jsonObject["msg"] = msg
            jsonObject["describe"] = describe
            jsonObject
        }
    }

    fun <T> getValidDes(item: T): List<Des> {
        val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
        val validator: Validator = factory.validator
        val validate: Set<ConstraintViolation<T>> = validator.validate(item)

        val collect: List<Des> = validate.map { violation ->
            val fieldName: String = violation.propertyPath.toString()
            val field = ReflectUtil.getField(violation.rootBeanClass, fieldName)
            val fieldComment = MetaInfoUtils.guessDescription(field)
            val message: String = violation.message
            Des(
                fieldName, fieldComment!!, StrUtil.addPrefixIfNot(message, fieldComment)
            )
        }.toMutableList()

        collect.addAll(getCustomDes(item))
        return collect
    }

    /**
     * 处理自定义注解的提示消息
     *
     * @param item 入参
     * @return [List]<[Des]>
     * @author zjarlin
     * @since 2023/10/07
     */
    private fun <T> getCustomDes(item: T): List<Des> {


        val java = item!!::class.java
        val fieldInfosRecursive = MetaInfoUtils.getFieldInfosRecursive(java)
        fieldInfosRecursive.map {
            val (declaringClass, field, description, fieldType, isNestedObject, children) = it
        }
        return TODO("提供返回值")
    }

}
