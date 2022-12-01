package com.nawilny.aoc2020.day03

import com.nawilny.aoc2020.common.Input

data class Slope(val lines: List<String>) {

    fun height() = lines.size

    fun isTree(p: Point): Boolean {
        if (p.y >= lines.size) {
            return false
        }
        val line = lines[p.y]
        val c = line[p.x % line.length]
        return c == '#'
    }

}

data class Point(val x: Int, val y: Int) {

    fun move(vector: Point) = Point(x + vector.x, y + vector.y)

}

//Right 1, down 1. 53
//Right 3, down 1. 167
//Right 5, down 1. 54
//Right 7, down 1. 67
//Right 1, down 2. 23

fun main() {
    val lines = Input.readFileLines("day03", "input1.txt")
            .map { it.trim() }
            .filter { it.isNotBlank() }
    val slope = Slope(lines)

    var p = Point(0, 0)
    val vector = Point(1, 2)
    var treeCounter = 0

    while (p.y <= slope.height()) {
        if (slope.isTree(p)) {
            treeCounter++
        }
        p = p.move(vector)
    }

    println(treeCounter)
}
