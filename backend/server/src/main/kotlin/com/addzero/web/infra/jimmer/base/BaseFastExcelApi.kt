package com.addzero.web.infra.jimmer.base

import cn.hutool.core.util.TypeUtil
import cn.hutool.extra.spring.SpringUtil
import cn.idev.excel.FastExcel
import cn.idev.excel.context.AnalysisContext
import cn.idev.excel.read.listener.ReadListener
import cn.idev.excel.util.ListUtils
import com.addzero.common.kt_util.isNotEmpty
import com.addzero.common.kt_util.isNotNew
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.query.specification.KSpecification
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile
import kotlin.reflect.KClass


public class ExcelDataListener<ExcelDTO>() : ReadListener<ExcelDTO> {

    /**
     * 缓存的数据
     */
    val caches: MutableList<ExcelDTO> = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT)

    /**
     * 这个每一条数据解析都会来调用
     */
    override fun invoke(input: ExcelDTO, context: AnalysisContext?) {
        caches.add(input)
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (caches.size >= BATCH_COUNT) {
            // 存储完成清理 list
            caches.clear()
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    override fun doAfterAllAnalysed(context: AnalysisContext?) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        println("所有数据解析完成")
    }


    companion object {
        /**
         * 每隔600条存储数据库,然后清理list,方便内存回收
         */
        private const val BATCH_COUNT: Int = 600
    }
}

interface BaseFastExcelApi<T : Any, Spec : KSpecification<T>, ExcelWriteDTO : Any> {


    private val sql: KSqlClient get() = SpringUtil.getBean<KSqlClient>(KSqlClient::class.java)

    fun toExcelWriteDTO(entity: T): ExcelWriteDTO
    fun toEntity(excelWriteDTO: ExcelWriteDTO): T

    @PostMapping("/import")
    fun import(@RequestPart file: MultipartFile): Int {
        val use = file.inputStream.use {
            val excelDataListener = ExcelDataListener<ExcelWriteDTO>()

            val excelWriteDTOCLASS = ExcelWriteDTOCLASS()
            FastExcel.read(it, excelWriteDTOCLASS, excelDataListener).sheet().doRead()
            val caches = excelDataListener.caches
            caches
        }
        val map = use
            .filter { it.isNotEmpty() }
            .filter { it.isNotNew() }
            .map { toEntity(it) }
        val totalAffectedRowCount = sql.saveEntities(map).totalAffectedRowCount
        return totalAffectedRowCount
    }

    private fun CLASS(): KClass<T> {
        return (TypeUtil.getTypeArgument(javaClass, 0) as Class<T>).kotlin
    }


    private fun ExcelWriteDTOCLASS(): Class<ExcelWriteDTO> {
        val parameterizedType = TypeUtil.getGenerics(this.javaClass)[1]
        val typeArgument = TypeUtil.getTypeArgument(parameterizedType, 2)
        val kClass = typeArgument as Class<ExcelWriteDTO>
        return kClass
    }


}
