    package site.addzero.generated.mcp

    import site.addzero.common.consts.sql
    import site.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import site.addzero.model.entity.ProductCategory
    import site.addzero.generated.isomorphic.ProductCategoryIso

    /**
     * ProductCategory MCP服务
     *
     * 提供productcategory相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class ProductCategoryMcpService  {

        @Tool(description = "保存productcategory数据到数据库")
        fun saveProductCategory(@ToolParam(description = "productcategory数据对象") entity: ProductCategoryIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(ProductCategory::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }