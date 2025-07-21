//package com.addzero.kmp.component.table.viewmodel.generic
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewmodel.compose.viewModel
//import org.koin.core.component.KoinComponent
//import org.koin.core.component.inject
//import org.koin.core.parameter.parametersOf
//import org.koin.core.qualifier.named
//import kotlin.reflect.KClass
//
///**
// * 泛型表格ViewModel工厂
// *
// * 使用工厂模式来创建泛型ViewModel，绕过Koin对泛型的限制
// */
//interface GenericTableViewModelFactory : KoinComponent {
//    fun <T : Any> create(
//        dataClass: KClass<T>,
//        dataLoader: suspend (page: Int, size: Int, sortColumn: String?, sortAscending: Boolean) -> DataResult<T>,
//        idExtractor: (T) -> Any
//    ): GenericTableViewModel<T>
//}
//
///**
// * 默认的泛型表格ViewModel工厂实现
// */
//class DefaultGenericTableViewModelFactory : GenericTableViewModelFactory {
//
//    override fun <T : Any> create(
//        dataClass: KClass<T>,
//        dataLoader: suspend (page: Int, size: Int, sortColumn: String?, sortAscending: Boolean) -> DataResult<T>,
//        idExtractor: (T) -> Any
//    ): GenericTableViewModel<T> {
//        return object : GenericTableViewModel<T>() {
//            override suspend fun loadDataFromSource(
//                page: Int,
//                size: Int,
//                sortColumn: String?,
//                sortAscending: Boolean,
//                filters: Map<String, Any>
//            ): DataResult<T> {
//                return dataLoader(page, size, sortColumn, sortAscending)
//            }
//
//            override fun getItemId(item: T): Any = idExtractor(item)
//        }
//    }
//}
//
///**
// * 使用Repository的泛型ViewModel工厂
// */
//class RepositoryBasedViewModelFactory : GenericTableViewModelFactory {
//
//    // 注入Repository工厂
//    private val repositoryFactory: GenericRepositoryFactory by inject()
//
//    override fun <T : Any> create(
//        dataClass: KClass<T>,
//        dataLoader: suspend (page: Int, size: Int, sortColumn: String?, sortAscending: Boolean) -> DataResult<T>,
//        idExtractor: (T) -> Any
//    ): GenericTableViewModel<T> {
//
//        val repository = repositoryFactory.create(dataClass)
//
//        return object : GenericTableViewModel<T>() {
//            override suspend fun loadDataFromSource(
//                page: Int,
//                size: Int,
//                sortColumn: String?,
//                sortAscending: Boolean,
//                filters: Map<String, Any>
//            ): DataResult<T> {
//                return repository.loadData(page, size, sortColumn, sortAscending, filters)
//            }
//
//            override fun getItemId(item: T): Any = idExtractor(item)
//        }
//    }
//}
//
///**
// * 泛型Repository接口
// */
//interface GenericRepository<T : Any> {
//    suspend fun loadData(
//        page: Int,
//        size: Int,
//        sortColumn: String?,
//        sortAscending: Boolean,
//        filters: Map<String, Any>
//    ): DataResult<T>
//
//    suspend fun save(item: T): T
//    suspend fun delete(id: Any)
//    suspend fun findById(id: Any): T?
//}
//
///**
// * 泛型Repository工厂
// */
//interface GenericRepositoryFactory {
//    fun <T : Any> create(dataClass: KClass<T>): GenericRepository<T>
//}
//
///**
// * Koin模块配置
// */
//object GenericTableKoinModule {
//
//    val module = org.koin.dsl.module {
//
//        // 注册工厂
//        single<GenericTableViewModelFactory> { DefaultGenericTableViewModelFactory() }
//        single<GenericRepositoryFactory> { DefaultGenericRepositoryFactory() }
//
//        // 注册具体类型的ViewModel（推荐方式）
//        viewModel { UserTableViewModel() }
//        viewModel { ProductTableViewModel() }
//
//        // 注册Repository实现
//        single<GenericRepository<User>>(named("UserRepository")) { UserRepository() }
//        single<GenericRepository<Product>>(named("ProductRepository")) { ProductRepository() }
//    }
//}
//
///**
// * 默认Repository工厂实现
// */
//class DefaultGenericRepositoryFactory : GenericRepositoryFactory {
//    override fun <T : Any> create(dataClass: KClass<T>): GenericRepository<T> {
//        // 这里可以根据类型返回不同的Repository实现
//        @Suppress("UNCHECKED_CAST")
//        return when (dataClass) {
//            User::class -> UserRepository() as GenericRepository<T>
//            Product::class -> ProductRepository() as GenericRepository<T>
//            else -> throw IllegalArgumentException("Unsupported data class: ${dataClass.simpleName}")
//        }
//    }
//}
//
///**
// * 用户Repository实现
// */
//class UserRepository : GenericRepository<User> {
//    override suspend fun loadData(
//        page: Int,
//        size: Int,
//        sortColumn: String?,
//        sortAscending: Boolean,
//        filters: Map<String, Any>
//    ): DataResult<User> {
//        // 实际的数据加载逻辑
//        val users = generateUsers()
//        return DataResult(users, users.size)
//    }
//
//    override suspend fun save(item: User): User = item
//    override suspend fun delete(id: Any) {}
//    override suspend fun findById(id: Any): User? = null
//
//    private fun generateUsers(): List<User> {
//        return (1..100).map { id ->
//            User(
//                id = id,
//                name = "用户$id",
//                age = (20..60).random(),
//                email = "user$id@example.com",
//                department = listOf("技术部", "产品部", "设计部").random(),
//                isActive = true
//            )
//        }
//    }
//}
//
///**
// * 产品Repository实现
// */
//class ProductRepository : GenericRepository<Product> {
//    override suspend fun loadData(
//        page: Int,
//        size: Int,
//        sortColumn: String?,
//        sortAscending: Boolean,
//        filters: Map<String, Any>
//    ): DataResult<Product> {
//        val products = generateProducts()
//        return DataResult(products, products.size)
//    }
//
//    override suspend fun save(item: Product): Product = item
//    override suspend fun delete(id: Any) {}
//    override suspend fun findById(id: Any): Product? = null
//
//    private fun generateProducts(): List<Product> {
//        return (1..50).map { id ->
//            Product(
//                id = id,
//                name = "产品$id",
//                category = "分类${(id % 5) + 1}",
//                price = (10.0..1000.0).random(),
//                stock = (0..100).random()
//            )
//        }
//    }
//}
//
//// ==================== Compose集成 ====================
//
///**
// * 获取泛型ViewModel的Compose函数
// */
//@Composable
//inline fun <reified T : Any> rememberGenericTableViewModel(
//    noinline dataLoader: suspend (page: Int, size: Int, sortColumn: String?, sortAscending: Boolean) -> DataResult<T>,
//    noinline idExtractor: (T) -> Any
//): GenericTableViewModel<T> {
//    val factory: GenericTableViewModelFactory = org.koin.compose.koinInject()
//
//    return remember {
//        factory.create(T::class, dataLoader, idExtractor)
//    }
//}
//
///**
// * 使用具体类型ViewModel的Compose函数（推荐）
// */
//@Composable
//fun rememberUserTableViewModel(): UserTableViewModel {
//    return org.koin.compose.viewmodel.koinViewModel()
//}
//
//@Composable
//fun rememberProductTableViewModel(): ProductTableViewModel {
//    return org.koin.compose.viewmodel.koinViewModel()
//}
//
///**
// * 通用的ViewModel获取函数
// */
//@Composable
//inline fun <reified VM : GenericTableViewModel<*>> rememberTypedTableViewModel(): VM {
//    return org.koin.compose.viewmodel.koinViewModel()
//}
