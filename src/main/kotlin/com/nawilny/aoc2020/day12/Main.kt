package com.nawilny.aoc2020.day12

import com.nawilny.aoc2020.common.Input
import kotlin.math.absoluteValue

data class Command(val operation: Char, val value: Int) {
    companion object {
        fun parse(line: String): Command {
            val operation = line[0]
            val value = line.substring(1).toInt()
            return Command(operation, value)
        }
    }
}

abstract class Ship {

    fun move(command: Command) {
        when (command.operation) {
            'N' -> move(Pair(0, command.value))
            'S' -> move(Pair(0, -command.value))
            'W' -> move(Pair(-command.value, 0))
            'E' -> move(Pair(command.value, 0))
            'R' -> rotate(command.value)
            'L' -> rotate(-command.value)
            'F' -> moveForward(command.value)
            else -> error("Unknown operation '${command.operation}'")
        }
    }

    protected abstract fun move(p: Pair<Int, Int>)

    protected abstract fun rotate(r: Int)

    protected abstract fun moveForward(distance: Int)

}

class Ship1 : Ship() {

    var position = Pair(0, 0)
    private var rotation = 0

    override fun move(p: Pair<Int, Int>) {
        position = Pair(position.first + p.first, position.second + p.second)
    }

    override fun rotate(r: Int) {
        rotation += r
        if (rotation < 0) {
            rotation += 360
        }
        rotation %= 360
    }

    override fun moveForward(distance: Int) {
        when (rotation) {
            0 -> move(Pair(0, distance))    // E
            90 -> move(Pair(-distance, 0))  // S
            180 -> move(Pair(0, -distance)) // W
            270 -> move(Pair(distance, 0))  // N
            else -> error("Unsupported rotation '$rotation'")
        }
    }

}

class Ship2 : Ship() {

    private var waypoint = Pair(10, 1)
    var position = Pair(0, 0)

    override fun move(p: Pair<Int, Int>) {
        waypoint = Pair(waypoint.first + p.first, waypoint.second + p.second)
    }

    override fun rotate(r: Int) {
        val rotation = if (r < 0) r + 360 else r
        val newX = when (rotation) {
            90 -> waypoint.second
            180 -> -waypoint.first
            270 -> -waypoint.second
            else -> error("Unsupported rotation '$rotation'")
        }
        val newY = when (rotation) {
            90 -> -waypoint.first
            180 -> -waypoint.second
            270 -> waypoint.first
            else -> error("Unsupported rotation '$rotation'")
        }
        waypoint = Pair(newX, newY)
    }

    override fun moveForward(distance: Int) {
        position = Pair(position.first + (distance * waypoint.first), position.second + (distance * waypoint.second))
    }

}

fun main() {
    val commands = Input.readFileLines("day12", "input.txt")
            .filter { it.isNotBlank() }
            .map { Command.parse(it) }

    val ship = Ship2()
    commands.forEach { ship.move(it) }
    println(ship.position.first.absoluteValue + ship.position.second.absoluteValue)
}
