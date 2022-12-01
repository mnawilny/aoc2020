package com.nawilny.aoc2020.day16

import com.nawilny.aoc2020.common.Input

data class TicketsInput(val fields: Map<String, List<IntRange>>, val myTicket: List<Int>, val nearbyTickets: List<List<Int>>)

fun parseInput(lines: List<String>): TicketsInput {
    var state = 0
    val fields = mutableMapOf<String, List<IntRange>>()
    val myTicket = mutableListOf<Int>()
    val nearbyTickets = mutableListOf<List<Int>>()
    lines.forEach { line ->
        when (state) {
            0 -> {
                if (line == "your ticket:") {
                    state = 1
                } else {
                    val field = line.split(":").map { it.trim() }
                    val ranges = field[1].split("or").map { it.trim() }.map { range ->
                        val rangeNumbers = range.split("-").map { it.trim().toInt() }
                        IntRange(rangeNumbers[0], rangeNumbers[1])
                    }
                    fields.put(field[0], ranges)
                }
            }
            1 -> {
                if (line == "nearby tickets:") {
                    state = 2
                } else {
                    myTicket.addAll(line.split(",").map { it.toInt() })
                }
            }
            2 -> {
                nearbyTickets.add(line.split(",").map { it.toInt() })
            }
        }
    }
    return TicketsInput(fields, myTicket, nearbyTickets)
}

fun getInvalidFields(ticket: List<Int>, fieldWithRanges: Map<String, List<IntRange>>): List<Int> {
    return ticket.mapNotNull { value ->
        val isValid = fieldWithRanges.values.flatten().any {
            it.contains(value)
        }
        if (isValid) null else value
    }
}

fun main() {
    val lines = Input.readFileLines("day16", "input.txt").filter { it.isNotBlank() }
    val input = parseInput(lines)

    val result = input.nearbyTickets.flatMap { getInvalidFields(it, input.fields) }.sum()
    println(result)

    val initialPossibleValues = 0 until input.fields.size
    val validTickets = input.nearbyTickets.filter { getInvalidFields(it, input.fields).isEmpty() }
    val possiblePositions = input.fields.entries
            .map { Pair(it.key, initialPossibleValues.toMutableSet()) }
            .toMap()

    validTickets.forEach { ticket ->
        ticket.withIndex().forEach { position ->
            input.fields
                    .filter { field -> !field.value.any { it.contains(position.value) } }
                    .map { it.key }
                    .forEach { possiblePositions[it]!!.remove(position.index) }
        }
    }

    possiblePositions.toList().sortedBy { it.second.size }.forEach { println(it) }
}

// train, [0] 163
// arrival track, [1] 73
// - departure track, [2] 67
// type, [3] 113
// duration, [4] 79
// route, [5] 101
// arrival platform, [6] 109
// - departure date, [7] 149
// arrival station, [8] 53
// - departure location, [9] 61
// zone, [10] 97
// price, [11] 89
// - departure station, [12] 103
// wagon, [13] 59
// class, [14] 71
// arrival location, [15] 83
// seat, [16] 151
// row, [17] 127
// - departure platform, [18] 157
// - departure time, [19] 107

// 1053686852011
