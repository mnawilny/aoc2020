package com.nawilny.aoc2020.day10

import com.nawilny.aoc2020.common.Input
import java.math.BigInteger

val cache = mutableMapOf<Int, BigInteger>()

fun arrangesBetween(start: Int, end: Int, numbers: Set<Int>): BigInteger {
    return cache.getOrPut(start) {
        if (start == end) {
            BigInteger.ONE
        } else {
            (1..3).map { start + it }
                    .filter { numbers.contains(it) }
                    .map { arrangesBetween(it, end, numbers) }
                    .fold(BigInteger.ZERO) {acc, i -> acc.plus(i)}
        }
    }
}

fun main() {
    val numbers = Input.readFileLines("day10", "input.txt")
            .filter { it.isNotBlank() }
            .map { it.toInt() }
            .sorted()

    // part 1
    val diffs = numbers.withIndex().map {
        val prev = if (it.index > 0) numbers[it.index - 1] else 0
        it.value - prev
    }

    val oneDiffs = diffs.count { it == 1 }
    val threeDiffs = diffs.count { it == 3 } + 1

    println(oneDiffs * threeDiffs)

    // part 2
    val end = numbers.maxOrNull()!! + 3
    println(arrangesBetween(0, end, numbers.toSet().plus(end)))
}
