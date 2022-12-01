package com.nawilny.aoc2020.day01

import com.nawilny.aoc2020.common.Input

fun main() {
    val numbers = Input.readFileLines("day01", "input1.txt").map { it.toInt() }
//    val n = findNumbersPair(numbers)
    val n = findNumbersTriple(numbers)
    println(n.fold(1){ acc, i -> acc * i})
}

fun findNumbersPair(numbers: List<Int>): List<Int> {
    numbers.forEach { n1 ->
        numbers.forEach { n2 ->
            if (n1 != n2 && n1 + n2 == 2020) {
                return listOf(n1, n2)
            }
        }
    }
    throw IllegalStateException("NOT FOUND")
}

fun findNumbersTriple(numbers: List<Int>): List<Int> {
    numbers.forEach { n1 ->
        numbers.forEach { n2 ->
            numbers.forEach { n3 ->
                if (n1 != n2 && n1 != n3 && n2 != n3 && n1 + n2 + n3 == 2020) {
                    return listOf(n1, n2, n3)
                }
            }
        }
    }
    throw IllegalStateException("NOT FOUND")
}
