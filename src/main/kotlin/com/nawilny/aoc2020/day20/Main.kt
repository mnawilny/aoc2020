package com.nawilny.aoc2020.day20

import com.nawilny.aoc2020.common.Input
import java.math.BigInteger

private data class Tile(val id: Int, val lines: List<String>) {

    val topBorder = lines[0]
    val bottomBorder = lines.last()
    val leftBorder = lines.map { it.first() }.joinToString("")
    val rightBorder = lines.map { it.last() }.joinToString("")

    val borders = setOf(topBorder, bottomBorder, leftBorder, rightBorder)

    fun flipHorizontally() = Tile(id, lines.map { it.reversed() })
    fun flipVertically() = Tile(id, lines.reversed())

    fun print() {
        lines.forEach { println(it) }
    }

    fun rotateRight(): Tile {
        val rotatedLines = lines.first().indices.map { mutableListOf<Char>() }
        (lines.first().indices).forEach { i ->
            lines.withIndex().forEach { line ->
                rotatedLines[i].add(line.value[i])
            }
        }
        return Tile(id, rotatedLines.map { it.reversed().joinToString("") })
    }

    fun matchLeft(border: String): Tile {
        return when {
            leftBorder == border -> this
            leftBorder == border.reversed() -> flipVertically()
            topBorder == border -> rotateRight().rotateRight().rotateRight().flipVertically()
            topBorder == border.reversed() -> rotateRight().rotateRight().rotateRight()
            rightBorder == border -> rotateRight().rotateRight().flipVertically()
            rightBorder == border.reversed() -> rotateRight().rotateRight()
            bottomBorder == border -> rotateRight()
            bottomBorder == border.reversed() -> rotateRight().flipVertically()
            else -> error("Something went wrong")
        }
    }

    fun matchTop(border: String): Tile {
        return when {
            leftBorder == border -> rotateRight().flipHorizontally()
            leftBorder == border.reversed() -> rotateRight()
            topBorder == border -> this
            topBorder == border.reversed() -> flipHorizontally()
            rightBorder == border -> rotateRight().rotateRight().rotateRight()
            rightBorder == border.reversed() -> rotateRight().rotateRight().rotateRight().flipHorizontally()
            bottomBorder == border -> rotateRight().rotateRight().flipHorizontally()
            bottomBorder == border.reversed() -> rotateRight().rotateRight()
            else -> error("Something went wrong")
        }
    }

    fun cutBorders(): Tile {
        return Tile(id, lines.subList(1, (lines.size - 1)).map { it.substring(1, it.length - 1) })
    }

}

private fun parse(lines: List<String>): List<Tile> {
    val tiles = mutableListOf<Tile>()
    var tileId: Int? = null
    var tileLines = mutableListOf<String>()
    lines.forEach { line ->
        if (line.startsWith("Tile")) {
            if (tileId != null) {
                tiles.add(Tile(tileId!!, tileLines))
                tileLines = mutableListOf()
            }
            tileId = line.substring(5, line.length - 1).toInt()
        } else {
            tileLines.add(line)
        }
    }
    if (tileId != null) {
        tiles.add(Tile(tileId!!, tileLines))
    }
    return tiles
}

private fun getMatchingTile(tileId: Int, border: String, tiles: List<Tile>): Tile? {
    val matching = tiles.filter { it.id != tileId && (it.borders.contains(border) || it.borders.contains(border.reversed())) }
    if (matching.size > 1) {
        error("Too many tiles")
    }
    return matching.firstOrNull()
}

private fun getRow(start: Tile, tiles: List<Tile>): List<Tile> {
    val row = mutableListOf(start)
    var current = start
    while (getMatchingTile(current.id, current.rightBorder, tiles) != null) {
        val t = getMatchingTile(current.id, current.rightBorder, tiles)!!.matchLeft(current.rightBorder)
        row.add(t)
        current = t
    }
    return row
}

private fun fitImages(topLeft: Tile, tiles: List<Tile>): List<List<Tile>> {
    val rows = mutableListOf<List<Tile>>()
    var current = topLeft
    while (getMatchingTile(current.id, current.bottomBorder, tiles) != null) {
        val t = getMatchingTile(current.id, current.bottomBorder, tiles)!!.matchTop(current.bottomBorder)
        rows.add(getRow(current, tiles))
        current = t
    }
    rows.add(getRow(current, tiles))
    return rows
}

private fun glueImages(pieces: List<List<Tile>>): List<String> {
    return pieces.flatMap { row ->
        val toGlue = row.map { it.cutBorders() }
        val gluedLines = toGlue.first().lines.indices.map { mutableListOf<Char>() }
        toGlue.forEach { tile ->
            tile.lines.withIndex().forEach { line ->
                gluedLines[line.index].addAll(line.value.asIterable())
            }
        }
        gluedLines.map { it.joinToString("") }
    }
}

private const val MONSTER_PATTERN = """                  #
#    ##    ##    ###
 #  #  #  #  #  #   """

private fun hasPattern(image: List<String>, pattern: List<String>, imageLineNo: Int, imageColNo: Int): Boolean {
    pattern.withIndex().forEach { patternLine ->
        val lineNo = imageLineNo + patternLine.index
        if (lineNo > image.size - 1) {
            return false
        }
        patternLine.value.withIndex().filter { it.value == '#' }.forEach { patternCol ->
            val colNo = imageColNo + patternCol.index
            if (colNo > image.first().length - 1) {
                return false
            }
            if (image[lineNo][colNo] != '#') {
                return false
            }
        }
    }
    return true
}

private fun countPatterns(image: List<String>, pattern: List<String>): Int {
    val linesRange = image.indices
    val colsRange = image.first().indices
    return linesRange.map { lineNo ->
        colsRange.map { colNo ->
            if (hasPattern(image, pattern, lineNo, colNo)) 1 else 0
        }.sum()
    }.sum()
}

fun main() {
    val lines = Input.readFileLines("day20", "input.txt").filter { it.isNotBlank() }
    val tiles = parse(lines)

    val corners = tiles.filter { tile ->
        tile.borders.filter { getMatchingTile(tile.id, it, tiles) != null }.count() == 2
    }

    // part 1
    println(corners.map { it.id }.fold(BigInteger.ONE) { acc, i -> acc * i.toBigInteger() })

    // part 2
    val topLeft = tiles.find { it.id == 2711 }!!.flipHorizontally()
//    val topLeft = tiles.find { it.id == 1951 }!!.rotateRight().rotateRight().flipHorizontally()

    val pieces = fitImages(topLeft, tiles)

    val image = Tile(0, glueImages(pieces))
    image.print()

    println("---------------------------------------------")
    val marked = image.lines.map { line -> line.filter { it == '#' }.count() }.sum()
    println(marked)

    println("---------------------------------------------")
    MONSTER_PATTERN.lines().forEach { println(it) }
    val monstersMarked = MONSTER_PATTERN.lines().map { line -> line.filter { it == '#' }.count() }.sum()
    println(monstersMarked)
    println("---------------------------------------------")

    val monstersCount = listOf(
            countPatterns(image.lines, MONSTER_PATTERN.lines()),
            countPatterns(image.flipHorizontally().lines, MONSTER_PATTERN.lines()),
            countPatterns(image.rotateRight().lines, MONSTER_PATTERN.lines()),
            countPatterns(image.rotateRight().flipHorizontally().lines, MONSTER_PATTERN.lines()),
            countPatterns(image.rotateRight().rotateRight().lines, MONSTER_PATTERN.lines()),
            countPatterns(image.rotateRight().rotateRight().flipHorizontally().lines, MONSTER_PATTERN.lines()),
            countPatterns(image.rotateRight().rotateRight().rotateRight().lines, MONSTER_PATTERN.lines()),
            countPatterns(image.rotateRight().rotateRight().rotateRight().flipHorizontally().lines, MONSTER_PATTERN.lines())
    )
    val maxMonstersCount = monstersCount.maxOrNull()!!
    println(monstersCount)
    println(maxMonstersCount)

    println("---------------------------------------------")
    println(marked - (monstersMarked * maxMonstersCount))
}
