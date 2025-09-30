@file:OptIn(kotlin.time.ExperimentalTime::class)

package site.addzero.generated.isomorphic

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual

@Serializable
data class ThingModelPropertyIso(
    val thingModel: ThingModelIso? = null,
    val identifier: String = "",
    val name: String = "",
    val description: String = "",
    val dataType: String = "",
    val required: Boolean = false,
    val minNormalValue: Double? = null,
    val maxNormalValue: Double? = null,
    val minWarningValue: Double? = null,
    val maxWarningValue: Double? = null,
    val dataPrecision: Int? = null,
    val accessMode: String = "",
    val sort: Int = 0,
    val id: Long? = null,
    val updateBy: SysUserIso? = null,
    val createBy: SysUserIso? = null,
    @Contextual val createTime: kotlinx.datetime.LocalDateTime = kotlin.time.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
    @Contextual val updateTime: kotlinx.datetime.LocalDateTime? = null
)