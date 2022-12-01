package com.nawilny.aoc2020.day24

import com.nawilny.aoc2020.common.Input

private data class Tile(var isBlack: Boolean = false)

private data class HexPosition(val x: Int, val y: Int) {
    fun e() = HexPosition(x + 1, y)
    fun w() = HexPosition(x - 1, y)
    fun se() = HexPosition(if (y % 2 == 0) x + 1 else x, y - 1)
    fun sw() = HexPosition(if (y % 2 == 0) x else x - 1, y - 1)
    fun ne() = HexPosition(if (y % 2 == 0) x + 1 else x, y + 1)
    fun nw() = HexPosition(if (y % 2 == 0) x else x - 1, y + 1)
    fun neighbours() = listOf(e(), w(), se(), sw(), ne(), nw())
}

private fun movePath(position: HexPosition, path: String): HexPosition {
    return when {
        path.isEmpty() -> position
        path.startsWith("se") -> movePath(position.se(), path.substring(2))
        path.startsWith("sw") -> movePath(position.sw(), path.substring(2))
        path.startsWith("ne") -> movePath(position.ne(), path.substring(2))
        path.startsWith("nw") -> movePath(position.nw(), path.substring(2))
        path.startsWith("e") -> movePath(position.e(), path.substring(1))
        path.startsWith("w") -> movePath(position.w(), path.substring(1))
        else -> error("Invalid path '$path'")
    }
}

private fun countBlackNeighbours(position: HexPosition, tiles: Map<HexPosition, Tile>): Int {
    return position.neighbours().mapNotNull { tiles[it] }.filter { it.isBlack }.count()
}

private fun cycle(tiles: Map<HexPosition, Tile>): Map<HexPosition, Tile> {
    val minX = tiles.map { it.key.x }.minOrNull()!! - 2
    val maxX = tiles.map { it.key.x }.maxOrNull()!! + 2
    val minY = tiles.map { it.key.y }.minOrNull()!! - 1
    val maxY = tiles.map { it.key.y }.maxOrNull()!! + 1
    return (minX..maxX).flatMap { x -> (minY..maxY).map { HexPosition(x, it) } }
            .filter { p ->
                val blackNeighbors = countBlackNeighbours(p, tiles)
                if (tiles[p]?.isBlack == true) {
                    blackNeighbors in 1..2
                } else {
                    blackNeighbors == 2
                }
            }.map { Pair(it, Tile(true)) }
            .toMap()
}

fun main() {
    val start = HexPosition(0, 0)
    var tiles = Input.readFileLines("day24", "input.txt")
            .filter { it.isNotBlank() }
            .map { movePath(start, it) }
            .fold(mutableMapOf<HexPosition, Tile>()) { map, position ->
                val tile = map.getOrPut(position) { Tile() }
                tile.isBlack = !tile.isBlack
                map
            }.toMap()

    (1..100).forEach { _ ->
        tiles = cycle(tiles)
    }

    println(tiles.count { it.value.isBlack })
}
