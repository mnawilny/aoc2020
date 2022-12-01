package com.nawilny.aoc2020.day18

import com.nawilny.aoc2020.common.Input
import java.math.BigInteger

private fun removeWrappingBrackets(expression: String): String {
    return if (expression.startsWith("(")) {
        val innerExpression = expression.substring(1, expression.length - 1)
        var bracketsCounter = 1
        innerExpression.forEach { c ->
            when (c) {
                '(' -> bracketsCounter++
                ')' -> bracketsCounter--
            }
            if (bracketsCounter == 0) {
                return expression
            }
        }
        innerExpression
    } else {
        expression
    }
}

private fun findLastOperatorIndex(expression: String, operator: Char): Int {
    var bracketsCounter = 0
    expression.reversed().withIndex().forEach { c ->
        when {
            c.value == '(' -> bracketsCounter--
            c.value == ')' -> bracketsCounter++
            bracketsCounter == 0 && c.value == operator -> return expression.length - c.index - 1
        }
    }
    return -1
}

private fun tryToExecuteOperator(expression: String, op: Char, operation: (BigInteger, BigInteger) -> BigInteger): BigInteger? {
    val plusOpIndex = findLastOperatorIndex(expression, op)
    return if (plusOpIndex >= 0) {
        val left = calculateExpression(expression.substring(0, plusOpIndex).trim())
        val right = calculateExpression(expression.substring(plusOpIndex + 1).trim())
        operation(left, right)
    } else {
        null
    }
}

private fun calculateExpression(expression: String): BigInteger {
    val normalised = removeWrappingBrackets(expression)

    val r1 = tryToExecuteOperator(normalised, '*') { a, b -> a * b }
    if (r1 != null) {
        return r1
    }
    val r2 = tryToExecuteOperator(normalised, '+') { a, b -> a + b }
    return r2 ?: normalised.toBigInteger()
}

fun main() {
    println(calculateExpression("1 + 2 * 3 + 4 * 5 + 6"))
    println(calculateExpression("2 * 3 + (4 * 5)"))
    println(calculateExpression("5 + (8 * 3 + 9 + 3 * 4 * 3)"))
    println(calculateExpression("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"))
    println(calculateExpression("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))

    println("----")

    val result = Input.readFileLines("day18", "input.txt").filter { it.isNotBlank() }
            .map { calculateExpression(it) }
            .fold(BigInteger.ZERO) { acc, i -> acc.plus(i) }
    println(result)
}
