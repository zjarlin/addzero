package com.addzero.ai.mcp

import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service
import kotlin.math.PI
import kotlin.math.pow

/**
 * 计算器服务 - MCP工具示例
 */
@Service
class CalculatorService {

    @Tool(description = "执行基本的数学运算：加法、减法、乘法、除法")
    fun calculate(expression: String): String {
        return try {
            // 简单的表达式计算（实际项目中应该使用更安全的表达式解析器）
            val result = when {
                "+" in expression -> {
                    val parts = expression.split("+")
                    parts[0].trim().toDouble() + parts[1].trim().toDouble()
                }
                "-" in expression -> {
                    val parts = expression.split("-")
                    parts[0].trim().toDouble() - parts[1].trim().toDouble()
                }
                "*" in expression -> {
                    val parts = expression.split("*")
                    parts[0].trim().toDouble() * parts[1].trim().toDouble()
                }
                "/" in expression -> {
                    val parts = expression.split("/")
                    val divisor = parts[1].trim().toDouble()
                    if (divisor == 0.0) {
                        return "错误：除数不能为零"
                    }
                    parts[0].trim().toDouble() / divisor
                }
                else -> {
                    return "不支持的运算符，请使用 +、-、*、/ 中的一个"
                }
            }
            
            "计算结果: $expression = $result"
        } catch (e: Exception) {
            "计算错误: ${e.message}"
        }
    }

    @Tool(description = "计算数字的平方根")
    fun sqrt(number: Double): String {
        return if (number < 0) {
            "错误：不能计算负数的平方根"
        } else {
            "√$number = ${sqrt(number)}"
        }
    }

    @Tool(description = "计算数字的幂")
    fun power(base: Double, exponent: Double): String {
        return try {
            val result = base.pow(exponent)
            "$base^$exponent = $result"
        } catch (e: Exception) {
            "计算错误: ${e.message}"
        }
    }

    @Tool(description = "计算圆的面积")
    fun circleArea(radius: Double): String {
        return if (radius <= 0) {
            "错误：半径必须大于0"
        } else {
            val area = PI * radius * radius
            "半径为 $radius 的圆的面积 = $area"
        }
    }

    @Tool(description = "生成指定范围内的随机数")
    fun randomNumber(min: Int, max: Int): String {
        return if (min >= max) {
            "错误：最小值必须小于最大值"
        } else {
            val random = (min..max).random()
            "在 $min 到 $max 之间的随机数: $random"
        }
    }
}
