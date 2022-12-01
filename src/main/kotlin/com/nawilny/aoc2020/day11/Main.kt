package com.nawilny.aoc2020.day11

import com.nawilny.aoc2020.common.Input

enum class PositionState(val char: Char) {
    FLOOR('.'),
    EMPTY_SEAT('L'),
    OCCUPIED_SEAT('#');

    companion object {
        fun fromChar(char: Char) = values().find { it.char == char } ?: error("State '$char' not found")
    }
}

data class Area(var seats: List<List<PositionState>>, val tolerance: Int) {

    private val colsRange = IntRange(0, seats[0].size - 1)
    private val rowsRange = IntRange(0, seats.size - 1)

    private val neighboursCache = mutableMapOf<Pair<Int, Int>, Set<Pair<Int, Int>>>()

    fun round(): Area {
        return Area(
                rowsRange.map { row ->
                    colsRange.map { col -> calculatePosition(row, col) }
                }, tolerance)
    }

    private fun calculatePosition(row: Int, col: Int): PositionState {
        return when (seats[row][col]) {
            PositionState.FLOOR -> PositionState.FLOOR
            PositionState.EMPTY_SEAT -> {
                if (countOccupied(row, col) == 0) {
                    PositionState.OCCUPIED_SEAT
                } else {
                    PositionState.EMPTY_SEAT
                }
            }
            PositionState.OCCUPIED_SEAT -> {
                if (countOccupied(row, col) >= tolerance) {
                    PositionState.EMPTY_SEAT
                } else {
                    PositionState.OCCUPIED_SEAT
                }
            }
        }
    }

    private fun countOccupied(row: Int, col: Int): Int {
        return calculateNeighbours2(Pair(row, col)).map { isOccupied(it) }.map { if (it) 1 else 0 }.sum()
    }

    private fun isOccupied(pos: Pair<Int, Int>): Boolean {
        return rowsRange.contains(pos.first) && colsRange.contains(pos.second) && seats[pos.first][pos.second] == PositionState.OCCUPIED_SEAT
    }

    private fun calculateNeighbours1(pos: Pair<Int, Int>): Set<Pair<Int, Int>> {
        return neighboursCache.getOrPut(pos) {
            setOf(
                    Pair(pos.first - 1, pos.second - 1),
                    Pair(pos.first - 1, pos.second),
                    Pair(pos.first - 1, pos.second + 1),
                    Pair(pos.first, pos.second - 1),
                    Pair(pos.first, pos.second + 1),
                    Pair(pos.first + 1, pos.second - 1),
                    Pair(pos.first + 1, pos.second),
                    Pair(pos.first + 1, pos.second + 1)
            )
        }
    }

    private fun calculateNeighbours2(pos: Pair<Int, Int>): Set<Pair<Int, Int>> {
        return neighboursCache.getOrPut(pos) {
            setOf(
                    findFirstSeat(pos) { Pair(it.first - 1, it.second - 1) },
                    findFirstSeat(pos) { Pair(it.first - 1, it.second) },
                    findFirstSeat(pos) { Pair(it.first - 1, it.second + 1) },
                    findFirstSeat(pos) { Pair(it.first, it.second - 1) },
                    findFirstSeat(pos) { Pair(it.first, it.second + 1) },
                    findFirstSeat(pos) { Pair(it.first + 1, it.second - 1) },
                    findFirstSeat(pos) { Pair(it.first + 1, it.second) },
                    findFirstSeat(pos) { Pair(it.first + 1, it.second + 1) }
            ).filterNotNull().toSet()
        }
    }

    private fun findFirstSeat(from: Pair<Int, Int>, operation: (Pair<Int, Int>) -> Pair<Int, Int>): Pair<Int, Int>? {
        var firstSeat = operation(from)
        while (true) {
            if (!rowsRange.contains(firstSeat.first) || !colsRange.contains(firstSeat.second)) {
                return null
            }
            if (seats[firstSeat.first][firstSeat.second] != PositionState.FLOOR) {
                return firstSeat
            }
            firstSeat = operation(firstSeat)
        }
    }

    fun countOccupied(): Int {
        return seats.map { row ->
            row.map { if (it == PositionState.OCCUPIED_SEAT) 1 else 0 }.sum()
        }.sum()
    }

    fun print() = seats.map { row -> row.map { it.char }.joinToString("") }.forEach { println(it) }

}

fun main() {
    val area = Area(Input.readFileLines("day11", "input.txt")
            .filter { it.isNotBlank() }
            .map { line -> line.trim().map { PositionState.fromChar(it) } }, 5)

    var prevArea = area
    var newArea = area.round()

    while (prevArea != newArea) {
        prevArea = newArea
        newArea = newArea.round()
    }

    newArea.print()
    println(newArea.countOccupied())
}
