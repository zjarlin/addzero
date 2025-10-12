# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Kotlin Multiplatform (KMP) full-stack scaffold project built with:
- **Backend**: Spring Boot + Jimmer ORM + PostgreSQL
- **Frontend**: Compose Multiplatform (Android, Desktop, iOS, WebAssembly)
- **Code Generation**: Extensive KSP (Kotlin Symbol Processing) for consistency between frontend and backend
- **Architecture**: Multi-module layered architecture with automatic code generation

## Build Commands

### Core Development Commands
```bash
# Build entire project
./gradlew build

# Build specific modules
./gradlew :backend:server:build
./gradlew :composeApp:build
./gradlew :lib:shadcn-compose-component:build

# Run backend server
./gradlew :backend:server:run

# Run Compose desktop app
./gradlew :composeApp:run

# Generate KSP code
./gradlew kspKotlin

# Clean and rebuild
./gradlew clean build
```

### Testing Commands
```bash
# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :backend:server:test
./gradlew :lib:shadcn-compose-component:test

# Run single test class
./gradlew :backend:server:test --tests "com.example.MyTest"
```

### Development Environment Setup
Required environment variables (add to .zshrc/.bashrc or PowerShell profile):
```bash
export SERVER_HOST=localhost
export DB_PORT=5432
export DATABASE_NAME=postgres
export SCHEMA_NAME=public
export JDBC_USERNAME=postgres
export JDBC_PASSWORD=postgres
export EXCLUDE_TABLES=flyway_schema_history,vector_store,*_mapping
```

## Architecture Overview

### Core Design Philosophy
- **Single Source of Truth**: Jimmer entities define the data model once
- **Type Safety**: KSP ensures compile-time type consistency across frontend/backend
- **Zero Boilerplate**: Automatic generation of CRUD APIs, forms, and UI components
- **Strategy Pattern**: Pluggable field type handlers for form generation

### Module Structure

#### KSP Processors (`lib/ksp/`, `lib/addzero-kcp-i18n/`)
- **addzero-ksp-support**: Base KSP utilities and metadata extraction
- **addzero-entity2form-processor**: Generates dynamic forms from Jimmer entities
- **addzero-controller2api-processor**: Generates type-safe API clients from Spring Controllers
- **addzero-route-processor**: Generates navigation tables from @Route annotations
- **addzero-jdbc2enum-processor**: Generates enums from database dictionary tables
- **addzero-compose-props-processor**: Generates reactive state management for @ComposeAssist

#### Shared Libraries (`lib/tool-kmp/`, `shared/`, `shared-compose/`)
- **addzero-tool**: Common utilities and code generation tools
- **shared**: Cross-platform data models and business logic
- **shared-compose**: Compose UI components shared across platforms

#### Component Library (`lib/shadcn-compose-component/`)
- Shadcn-style UI components for Compose Multiplatform
- Consistent theming with `MaterialTheme.colors` → use `colors` directly
- Components include: Button, Input, Search, Dialog, Drawer, etc.

#### Applications (`composeApp/`, `backend/`)
- **composeApp**: Main Compose Multiplatform application
- **backend/server**: Spring Boot backend server
- **backend/model**: Backend data models and entities

### KSP Configuration

#### Entity-to-Form Generation
```kotlin
// In build.gradle.kts
ksp {
    arg("entityPackage", "site.addzero.entity")
    arg("formOutputPackage", "site.addzero.generated.forms")
    arg("apiOutputPackage", "site.addzero.generated.api")
}
```

#### Form Generation Strategies
The form generator uses a strategy pattern for different field types:
- `MoneyStrategy`: Currency fields with automatic currency symbols
- `PercentageStrategy`: Percentage input fields
- `PhoneStrategy`: Phone number validation
- `EmailStrategy`: Email validation with checking
- `IntegerStrategy`, `DecimalStrategy`: Numeric input
- And more...

### Code Generation Patterns

#### Jimmer Entity → Form Generation
```kotlin
@Entity
interface UserProfile {
    @Id val id: Long
    /** 用户名 */ // Documentation comment becomes label if no @Label
    val username: String
    @Label("邮箱") val email: String
}

// Automatically generates:
@Composable
fun UserProfileForm(
    state: MutableState<UserProfileIso>,
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    dslConfig: UserProfileFormDsl.() -> Unit = {}  // DSL customization
) { /* ... */ }
```

#### @ComposeAssist Pattern
Generates reactive state management:
```kotlin
@ComposeAssist
@Composable
fun Counter(
    count: Int = 0,
    onIncrement: () -> Unit = {}
) { /* ... */ }

// Generates:
class CounterState(count: Int, onIncrement: () -> Unit) {
    private val _count = mutableStateOf(count)
    var count: Int by _count  // Reactive property
}

@Composable
fun rememberCounterState(...): CounterState { /* ... */ }
```

### Color System Consistency
**Important**: Always use `colors` directly, not `MaterialTheme.colors`:
```kotlin
// ✅ Correct
colors.primary
colors.background
colors.foreground

// ❌ Incorrect
MaterialTheme.colors.primary
```

This is already imported as `import site.addzero.themes.colors` in component files.

### Component Library Usage
The shadcn-compose-component library provides ready-to-use components:
- Use `Button` with variants: Default, Destructive, Outline, Secondary, Ghost, Link
- Use `SearchInput` variants: Default, Ghost, Outline with different sizes
- All components follow the `colors` convention for theming

### Common Issues and Solutions

#### KSP Generation Errors
- **"Error type <ERROR TYPE>"**: Check if controller parameters/return types are in shared directory (needed for client generation)
- **"Expecting an expression"**: May occur with @ManyToOne relationships without nullable specification
- **"Unresolved reference 'name'"**: Add @LabelProp annotation to Jimmer entity for search fields

#### Build Issues
- If KSP generated files are outdated, delete them and regenerate: `./gradlew clean kspKotlin`
- For dependency issues, run `./gradlew --refresh-dependencies`

### Database Integration
- Database auto-initializes on first backend startup
- Uses Flyway for migrations
- Requires PostgreSQL with pgvector extension for AI features
- Entity changes automatically propagate to generated forms and APIs

### Development Workflow
1. Define/modify Jimmer entities in backend
2. Run KSP generation to update forms, APIs, and models
3. Use generated components with DSL customization as needed
4. Components automatically handle validation, styling, and state management
5. All changes are type-safe across frontend and backend

This architecture enables rapid development with strong type safety and consistent UI/UX across all platforms.

## Development Guidelines

### Component Design Principles

#### 🎯 Avoid Data Classes, Prefer Interfaces + Generics
**Rule**: 尽量不要定义数据类，都用泛型+函数式接口

**Why**: This approach provides:
- Better decoupling between component library and business logic
- More flexibility for consumers to implement their own data structures
- Reduced dependency surface area
- Easier testing and mocking

**Example**:
```kotlin
// ❌ Avoid: Defining concrete data classes
data class SearchResultItem<T>(
    val id: String,
    val title: String,
    val data: T? = null
)

// ✅ Prefer: Interface-based design
interface SearchResultItem<T> {
    val id: String
    val title: String
    val data: T?
}

// Usage in business code
val searchItems = menuItems.map { menuItem ->
    object : SearchResultItem<SysMenuVO> {
        override val id: String = menuItem.path
        override val title: String = menuItem.title
        override val data: SysMenuVO = menuItem
    }
}
```

#### 🔧 Interface-Driven Component Architecture
**Pattern**: All component definitions should have an abstract interface (ViewModel) to decouple the library from user business modules.

**Benefits**:
- Complete separation of concerns
- Type-safe contracts
- Business logic remains in consumer code
- Component library stays pure and reusable

**Example**:
```kotlin
// Component library - pure interface
interface SearchViewModel<T> {
    suspend fun search(query: String): List<SearchResultItem<T>>
    suspend fun clearSearch()
    fun onResultClick(result: SearchResultItem<T>)
}

// Business implementation
class MenuSearchViewModel : SearchViewModel<SysMenuVO> {
    // Business-specific implementation
}
```

### Implementation Guidelines

1. **Component Library**: Only contain interfaces and atomic components
2. **Business Code**: Implement interfaces using anonymous objects or dedicated classes
3. **Type Safety**: Maintain compile-time type checking through generics
4. **Zero Business Dependencies**: Component library should never import business modules
5. **Function-First Design**: Prefer functional interfaces over class hierarchies

This design philosophy ensures maximum reusability and maintainability across different business contexts.


尽量封装泛型+lambda组件,不要定义数据类
每次完成任务编译一下获取报错自己解决

渲染函数逻辑尽量拆分为一个个小函数

