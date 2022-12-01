package com.nawilny.aoc2020.day05

import com.nawilny.aoc2020.common.Input

fun convertToDec(s: String): Int {
    val bin = s.replace('F', '0').replace('B', '1').replace('L', '0').replace('R', '1')
    var result = 0
    var multiplier = 1
    bin.reversed().forEach {
        val digit = if (it == '1') 1 else 0
        result += digit * multiplier
        multiplier *= 2
    }
    return result
}

fun main() {
    val seats = Input.readFileLines("day05", "input.txt")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { convertToDec(it) }
    val range = IntRange(seats.minOrNull()!!, seats.maxOrNull()!!)
    for (i in range) {
        if (!seats.contains(i) && seats.contains(i - 1) && seats.contains(i + 1)) {
            println(i)
        }
    }
}
