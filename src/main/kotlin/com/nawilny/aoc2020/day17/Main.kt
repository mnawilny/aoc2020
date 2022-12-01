package com.nawilny.aoc2020.day17

import com.nawilny.aoc2020.common.Input

private data class Point4D(val x: Int, val y: Int, val z: Int, val w: Int)

private data class Space(val activePoints: Set<Point4D>) {

    fun cycle(): Space {
        val newActivePoints = mutableSetOf<Point4D>()

        val minX = activePoints.map { it.x }.minOrNull()!!
        val maxX = activePoints.map { it.x }.maxOrNull()!!
        val minY = activePoints.map { it.y }.minOrNull()!!
        val maxY = activePoints.map { it.y }.maxOrNull()!!
        val minZ = activePoints.map { it.z }.minOrNull()!!
        val maxZ = activePoints.map { it.z }.maxOrNull()!!
        val minW = activePoints.map { it.w }.minOrNull()!!
        val maxW = activePoints.map { it.w }.maxOrNull()!!

        (minX - 1..maxX + 1).forEach { x ->
            (minY - 1..maxY + 1).forEach { y ->
                (minZ - 1..maxZ + 1).forEach { z ->
                    (minW - 1..maxW + 1).forEach { w ->
                        val point = Point4D(x, y, z, w)
                        val activeNeighbors = getNeighbours(point).filter { activePoints.contains(it) }.count()
                        if (activePoints.contains(point)) {
                            if (activeNeighbors == 2 || activeNeighbors == 3) {
                                newActivePoints.add(point)
                            }
                        } else {
                            if (activeNeighbors == 3) {
                                newActivePoints.add(point)
                            }
                        }
                    }
                }
            }
        }

        return Space(newActivePoints)
    }

    private fun getNeighbours(p: Point4D): Set<Point4D> {
        return (p.x - 1..p.x + 1).flatMap { x ->
            (p.y - 1..p.y + 1).flatMap { y ->
                (p.z - 1..p.z + 1).flatMap { z ->
                    (p.w - 1..p.w + 1).map { w ->
                        Point4D(x, y, z, w)
                    }
                }
            }
        }.filter { it.x != p.x || it.y != p.y || it.z != p.z || it.w != p.w }.toSet()
    }


    fun countActive() = activePoints.size

}

private fun parseInput(lines: List<String>): Space {
    val activePoints = lines.withIndex().map { line ->
        line.value.withIndex()
                .filter { it.value == '#' }
                .map { Point4D(it.index, line.index, 0, 0) }
    }.flatten().toSet()
    return Space(activePoints)
}

fun main() {
    val lines = Input.readFileLines("day17", "input.txt").filter { it.isNotBlank() }
    val space = parseInput(lines)
    val resultSpace = (1..6).fold(space) { s, i ->
        println("calculating $i cycle")
        s.cycle()
    }
    println(resultSpace)
    println(resultSpace.countActive())
}
