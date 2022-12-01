package com.nawilny.aoc2020.day02

import com.nawilny.aoc2020.common.Input

data class Rule(val char: Char, val range: IntRange) {

    fun validate1(password: String): Boolean {
        val c = password.count { it == char }
        return range.contains(c)
    }

    fun validate2(password: String): Boolean {
        return password.containsCharAt(char, range.first).xor(password.containsCharAt(char, range.last))
    }

    private fun String.containsCharAt(c: Char, position: Int) = this.length >= position && this[position - 1] == c

    companion object {
        fun parse(s: String): Rule {
            val parts = s.trim().split(" ")
            if (parts.size != 2) {
                throw IllegalStateException("Invalid rule $s")
            }
            val repeats = parts[0].split("-")
            if (repeats.size != 2) {
                throw IllegalStateException("Invalid rule $s")
            }
            return Rule(parts[1].toCharArray()[0], IntRange(start = repeats[0].toInt(), endInclusive = repeats[1].toInt()))
        }
    }
}

// 649

fun main() {
    val result = Input.readFileLines("day02", "input1.txt")
            .map { it.split(":") }
            .map { Rule.parse(it[0]) to it[1].trim() }
            .count { it.first.validate2(it.second) }
    println(result)
}
