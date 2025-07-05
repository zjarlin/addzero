import com.addzero.kmp.context.Settings
import com.addzero.kmp.util.BeanUtil.mapToBean
import com.addzero.kmp.util.BeanUtil.toMap
import java.util.concurrent.atomic.AtomicReference

object SettingContext {
    private val settingsRef = AtomicReference<Settings?>()

    val settings: Settings
        get() = settingsRef.get() ?: Settings()

    fun initialize(op: Map<String, String>) {
        val toMap = settings.toMap()
        val map = toMap + op
        val mapToBean = mapToBean<Settings>(map)
        settingsRef.compareAndSet(null, mapToBean)

//         Settings(
//            dbType = op["dbType"] ?: throw IllegalArgumentException("ksp option dbType required"),
//            idType = op["idType"] ?: throw IllegalArgumentException("ksp option idType required"),
//            id = op["id"] ?: throw IllegalArgumentException("ksp option id required"),
//            createBy = op["createBy"] ?: throw IllegalArgumentException("ksp option createBy required"),
//            updateBy = op["updateBy"] ?: throw IllegalArgumentException("ksp option updateBy required"),
//            createTime = op["createTime"] ?: throw IllegalArgumentException("ksp option createTime required"),
//            updateTime = op["updateTime"] ?: throw IllegalArgumentException("ksp option updateTime required"),
//            skipExistsFiles = op["skipExistsFiles"] ?: throw IllegalArgumentException("ksp option skipExistsFiles required"),
//            sharedSourceDir = op["sharedSourceDir"] ?: throw IllegalArgumentException("ksp option sharedSourceDir required"),
//            isomorphicPackageName = op["isomorphicPackageName"] ?: throw IllegalArgumentException("ksp option isomorphicPackageName required"),
//            isomorphicClassSuffix = op["isomorphicClassSuffix"] ?: throw IllegalArgumentException("ksp option isomorphicClassSuffix required"),
//            formPackageName = op["formPackageName"] ?: throw IllegalArgumentException("ksp option formPackageName required"),
//            apiClientPackageName = op["apiClientPackageName"] ?: throw IllegalArgumentException("ksp option apiClientPackageName required")
//        )
//        settingsRef.compareAndSet(null, newSettings)
    }
}