# AddGenericTable ViewModel 重构说明

## 重构背景

原有的AddGenericTable组件中，各个子ViewModel的参数传递散乱，配置分散在不同的地方，维护困难。本次重构旨在：

1. **统一配置管理**：将所有ViewModel的配置参数集中到一个配置类中
2. **简化参数传递**：通过配置对象统一初始化各个子ViewModel
3. **提高可维护性**：配置结构清晰，便于扩展和修改
4. **增强类型安全**：使用强类型配置，减少运行时错误

## 重构内容

### 1. 新增配置类

#### TableViewModelConfig

集成所有子ViewModel的配置参数：

```kotlin
data class TableViewModelConfig(
    val paginationConfig: TablePaginationConfig = TablePaginationConfig(),
    val searchConfig: TableSearchConfig = TableSearchConfig(),
    val selectionConfig: TableSelectionConfig = TableSelectionConfig(),
    val sortConfig: TableSortConfig = TableSortConfig()
)
```

#### 各子配置类

- `TablePaginationConfig`: 分页相关配置
- `TableSearchConfig`: 搜索相关配置
- `TableSelectionConfig`: 选择相关配置
- `TableSortConfig`: 排序相关配置

### 2. 修改子ViewModel

每个子ViewModel都新增了`initialize(config)`方法：

```kotlin
// 示例：TablePaginationViewModel
@KoinViewModel
class TablePaginationViewModel : ViewModel() {
    private var config: TablePaginationConfig = TablePaginationConfig()
    
    fun initialize(paginationConfig: TablePaginationConfig) {
        config = paginationConfig
        pageState = pageState.copy(pageSize = config.defaultPageSize)
    }
}
```

### 3. 更新TableCompositeViewModel

统一初始化所有子ViewModel：

```kotlin
fun initialize(tableConfig: TableConfig) {
    config = tableConfig
    
    // 初始化各个子ViewModel
    dataViewModel.initialize(config)
    paginationViewModel.initialize(config.viewModelConfig.paginationConfig)
    searchViewModel.initialize(config.viewModelConfig.searchConfig)
    selectionViewModel.initialize(config.viewModelConfig.selectionConfig)
    sortViewModel.initialize(config.viewModelConfig.sortConfig)
    uiStateViewModel.initialize(config.uiConfig)
    
    // 如果没有初始数据，则加载数据
    if (config.dataConfig.initData.isEmpty()) {
        queryPage()
    }
}
```

### 4. 新增配置构建器

提供链式调用的配置构建方式：

```kotlin
val config = tableConfig {
    viewModel {
        pagination {
            defaultPageSize(20)
            pageSizeOptions(listOf(10, 20, 50, 100))
        }
        
        search {
            enableAdvancedSearch(true)
            autoSearch(false)
        }
        
        selection {
            enableMultiSelect(true)
            defaultEditMode(false)
        }
        
        sort {
            enableSort(true)
            defaultSortColumn("createTime")
            defaultSortDirection(EnumSortDirection.DESC)
        }
    }
}
```

## 使用方式

### 基础用法

```kotlin
@Composable
fun MyTableScreen() {
    val config = tableConfig {
        data {
            columns(myColumns)
            getIdFun { it.id }
        }
        
        api {
            onLoadData { input -> myApiService.loadData(input) }
            onSave { item -> myApiService.save(item) }
            onDelete { id -> myApiService.delete(id) }
        }
        
        viewModel {
            pagination {
                defaultPageSize(20)
            }
        }
    }
    
    AddGenericTable(
        config = config,
        modifier = Modifier.fillMaxSize()
    )
}
```

### 高级用法

```kotlin
val advancedConfig = tableConfig {
    // 数据配置
    data {
        initData(initialData)
        columns(complexColumns)
        getIdFun { it.uniqueId }
        onRowClick { row -> navigateToDetail(row) }
    }
    
    // API配置
    api {
        onLoadData { input -> 
            // 复杂的数据加载逻辑
            repository.loadWithFilters(input)
        }
        onCustomEvent { event, data ->
            when (event) {
                "refresh" -> handleRefresh()
                "export" -> handleExport(data)
                else -> false
            }
        }
    }
    
    // ViewModel配置
    viewModel {
        pagination {
            defaultPageSize(50)
            pageSizeOptions(listOf(25, 50, 100, 200))
        }
        
        search {
            enableAdvancedSearch(true)
            autoSearch(true)
            autoSearchDelay(300)
            searchPlaceholder("搜索用户、订单、商品...")
        }
        
        selection {
            enableMultiSelect(true)
            defaultEditMode(false)
        }
        
        sort {
            enableSort(true)
            enableMultiSort(true)
            defaultSortColumn("updateTime")
            defaultSortDirection(EnumSortDirection.DESC)
        }
    }
}
```

## 优势

### 1. 配置集中化

- 所有ViewModel配置都在一个地方管理
- 便于理解和维护整个表格的行为

### 2. 类型安全

- 强类型配置，编译时检查
- 减少运行时配置错误

### 3. 灵活性

- 支持部分配置，未配置的使用默认值
- 链式调用，配置语法简洁

### 4. 可扩展性

- 新增配置项只需修改对应的配置类
- 不影响现有代码

### 5. 代码复用

- 配置可以保存和复用
- 便于创建配置模板

## 迁移指南

### 原有代码

```kotlin
// 原来需要在多个地方设置参数
val tableViewModel = koinViewModel<TableCompositeViewModel>()
tableViewModel.initialize(config)
// 其他ViewModel参数散落在各处
```

### 新代码

```kotlin
// 现在统一在配置中设置
val config = tableConfig {
    viewModel {
        pagination { defaultPageSize(20) }
        search { enableAdvancedSearch(true) }
        // ... 其他配置
    }
}

AddGenericTable(config = config)
```

## 注意事项

1. **向后兼容**：现有的TableConfig仍然可用，只是新增了viewModelConfig字段
2. **默认值**：所有配置都有合理的默认值，可以渐进式配置
3. **性能**：配置对象在初始化时创建，运行时无额外开销
4. **测试**：配置对象便于单元测试，可以轻松创建测试用的配置

## 示例代码

详细的使用示例请参考：

- `TableConfigExample.kt` - 各种配置场景的示例
- `TableConfigBuilder.kt` - 构建器的实现细节
