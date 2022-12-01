package com.nawilny.aoc2020.day09

import com.nawilny.aoc2020.common.Input
import java.math.BigInteger

fun isValid(n: BigInteger, lastNumbers: Set<BigInteger>): Boolean {
    lastNumbers.forEach {
        if (lastNumbers.contains(n.minus(it))) {
            return true
        }
    }
    return false
}

fun findInvalidNumber(numbers: List<BigInteger>, preamble: Int): BigInteger {
    numbers.withIndex()
            .filter { it.index >= preamble }
            .forEach { n ->
                val lastNumbers = numbers.subList(n.index - preamble, n.index).toSet()
                if (!isValid(n.value, lastNumbers)) {
                    return n.value
                }
            }
    error("Invalid number not found")
}

fun findSummingNumbers(numbers: List<BigInteger>, sum: BigInteger): List<BigInteger> {
    numbers.withIndex().forEach { start ->
        var currentSum = start.value
        var i = start.index + 1
        while (currentSum < sum && i < numbers.size) {
            currentSum = currentSum.plus(numbers[i])
            i++
        }
        if (i > 1 && currentSum == sum) {
            return numbers.subList(start.index, i)
        }
    }
    error("Summing numbers not found")
}

fun main() {
    val numbers = Input.readFileLines("day09", "input.txt")
            .filter { it.isNotBlank() }
            .map { it.toBigInteger() }

    val preamble = 25

    val invalid = findInvalidNumber(numbers, preamble)

    println(invalid)

    val summingNumbers = findSummingNumbers(numbers, invalid)

    println(summingNumbers)

    val result = summingNumbers.minOrNull()!! + summingNumbers.maxOrNull()!!

    println(result)
}
