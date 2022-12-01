package com.nawilny.aoc2020.day06

import com.nawilny.aoc2020.common.Input

data class Group(val answers: List<String>) {

    fun any(): Set<Char> {
        return answers.map { it.toSet() }
                .fold(setOf()) { acc, set -> acc.plus(set) }
    }

    fun all(): Set<Char> {
        return answers.map { it.toSet() }
                .fold(CharRange('a', 'z').toSet()) { acc, set ->
                    var result = acc.toMutableSet()
                    acc.forEach {
                        if (!set.contains(it)) {
                            result.remove(it)
                        }
                    }
                    result
                }
    }

}

fun parse(lines: List<String>): List<Group> {
    var answers = mutableListOf<String>()
    val groups = mutableListOf<Group>()
    lines.forEach { line ->
        if (line.isBlank()) {
            if (answers.isNotEmpty()) {
                groups.add(Group(answers))
                answers = mutableListOf()
            }
        } else {
            answers.add(line)
        }
    }
    if (answers.isNotEmpty()) {
        groups.add(Group(answers))
    }
    return groups
}

fun main() {
    val lines = Input.readFileLines("day06", "input.txt")
    val answer = parse(lines).map { it.all().size }.sum()
    println(answer)
}
