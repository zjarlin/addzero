package site.addzero.kcp.i18n.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.DeprecatedForRemovalCompilerApi
import org.jetbrains.kotlin.backend.common.extensions.FirIncompatiblePluginAPI
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irGetObject
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.getPropertyGetter
import org.jetbrains.kotlin.name.Name
import java.io.File

/**
 * IR生成扩展，用于处理字符串字面量的国际化替换
 */
@OptIn(DeprecatedForRemovalCompilerApi::class, UnsafeDuringIrConstructionAPI::class, ObsoleteDescriptorBasedAPI::class)
class I18NIrGenerationExtension(
    private val targetLocale: String = "en",
    private val resourceBasePath: String = "i18n"
) : IrGenerationExtension {

    private val resourceManager = ResourceBundleManager()

    // 缓存t函数符号
    private var tFunctionSymbol: IrSimpleFunctionSymbol? = null

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.files.forEach { file ->
            // 处理文件中的字符串字面量
            processStringLiterals(file, pluginContext)
        }
    }

    /**
     * 处理字符串字面量的国际化
     */
    private fun processStringLiterals(file: IrFile, pluginContext: IrPluginContext) {
        // 获取文件名（不含扩展名）
        val fileName = File(file.fileEntry.name).nameWithoutExtension

        file.transform(object : IrElementTransformerVoid() {
            private var currentFunction: IrFunction? = null
            private var currentCall: IrFunctionAccessExpression? = null

            override fun visitFunction(declaration: IrFunction): IrExpression {
                currentFunction = declaration
                try {
                    super.visitFunction(declaration)
                } finally {
                    currentFunction = null
                }
                return declaration as IrExpression
            }

            override fun visitFunctionAccess(expression: IrFunctionAccessExpression): IrExpression {
                currentCall = expression
                try {
                    return super.visitFunctionAccess(expression)
                } finally {
                    currentCall = null
                }
            }

            override fun visitConst(expression: IrConst): IrExpression {
                // 检查是否为字符串常量
                if (expression.kind == IrConstKind.String) {
                    val stringValue = expression.value as String

                    // 如果字符串不是空的
                    if (stringValue.isNotEmpty()) {
                        // 获取组件名（如果在函数调用中）
                        val componentName = try {
                            currentCall?.symbol?.owner?.name?.asString()
                        } catch (e: Exception) {
                            null
                        }

                        // 获取函数名
                        val functionName = try {
                            currentFunction?.name?.asString()
                        } catch (e: Exception) {
                            null
                        }

                        // 生成资源键，按照要求的格式：文件名_函数名_组件名_参数名
                        val resourceKey = generateResourceKey(
                            fileName,
                            functionName,
                            componentName,
                            stringValue
                        )

                        println("[I18N] Generated resource key: '$resourceKey' for string: '$stringValue'")

                        // 尝试从资源文件中获取翻译
                        val translatedValue = resourceManager.getTranslation(
                            pluginContext,
                            resourceKey,
                            targetLocale,
                            resourceBasePath
                        )

                        // 如果找到了翻译，则替换字符串字面量
                        if (translatedValue != null) {
                            println("[I18N] Translated '$stringValue' to '$translatedValue'")
                            return IrConstImpl.string(
                                expression.startOffset,
                                expression.endOffset,
                                expression.type,
                                translatedValue
                            )
                        } else {
                            println("[I18N] No translation found for '$stringValue', keeping original")
                        }
                    }
                }

                return super.visitConst(expression)
            }
        }, null)
    }

    /**
     * 生成符合要求格式的资源键
     * 格式: 文件名_函数名_组件名_参数名
     */
    private fun generateResourceKey(fileName: String, functionName: String?, componentName: String?, stringValue: String): String {
        return buildString {
            append(fileName)
            if (functionName != null) {
                append("_")
                append(functionName)
            }
            if (componentName != null) {
                append("_")
                append(componentName)
            }
            append("_")
            // 对于参数名，我们使用固定的"text"，因为IR层面难以准确获取参数名
            append("text")
            append("_")
            append(stringValue)
        }
    }

    /**
     * 创建t函数调用
     */
    private fun createTFunctionCall(pluginContext: IrPluginContext, originalExpression: IrConst, resourceKey: String): IrExpression {
        // 直接创建对 site.addzero.util.I8nutil.t(string) 的调用
        val tFunctionSymbol = findTFunction(pluginContext)
        val builder = DeclarationIrBuilder(pluginContext, tFunctionSymbol)
        return builder.irCall(tFunctionSymbol).apply {
            // 创建字符串参数
            putValueArgument(0, builder.irString(resourceKey))
        }
    }

    /**
     * 查找t函数
     */
    @OptIn(FirIncompatiblePluginAPI::class)
    @Suppress("DEPRECATION") // 目前在K2编译器中暂无替代方法
    private fun findTFunction(pluginContext: IrPluginContext): IrSimpleFunctionSymbol {
        // 查找 site.addzero.util.I8nutil 对象中的 t 函数
        val i8nutilClass = pluginContext.referenceClass(FqName("site.addzero.util.I8nutil")) ?: error("无法找到 site.addzero.util.I8nutil 类")
        // 查找该类中的 t 函数
        val tFunction = i8nutilClass.owner.declarations
            .filterIsInstance<IrFunction>()
            .find { function -> function.name.asString() == "t" }
        if (tFunction != null) {
            return tFunction.symbol as IrSimpleFunctionSymbol
        }

        // 如果找不到，抛出异常
        error("无法找到 site.addzero.util.I8nutil.t 函数")
    }
}