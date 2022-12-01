package com.nawilny.aoc2020.day23

import java.math.BigInteger

private data class Cup(val value: Int, var next: Cup?, var inCircle: Boolean = true)

private class Cups(var current: Cup) {

    val cupMap: Map<Int, Cup>
    val max: Int

    init {
        cupMap = mutableMapOf(current.value to current)
        var m = current.value
        var x = current.next!!
        while (x.value != current.value) {
            cupMap[x.value] = x
            x = x.next!!
            if (x.value > m) {
                m = x.value
            }
        }
        max = m
    }

    fun pickNextToCurrent(): Cup {
        val n = current.next!!
        current.next = n.next
        n.inCircle = false
        return n
    }

    fun moveCurrentByOne() {
        current = current.next!!
    }

    fun insertAfter(cup: Cup, toInsert: Cup) {
        toInsert.next = cup.next
        toInsert.inCircle = true
        cup.next = toInsert
    }

    fun findDestination(): Cup {
        var i = current.value - 1
        while (true) {
            val c = cupMap[i]
            if (c != null && c.inCircle) {
                return c
            }
            i--
            if (i < 1) {
                i = max
            }
        }
    }

    fun toList(): List<Int> {
        val list = mutableListOf(current.value)
        var x = current.next!!
        while (x.value != current.value) {
            list.add(x.value)
            x = x.next!!
        }
        return list
    }

    fun getResult1(): String {
        var x = current
        while (x.value != 1) {
            x = x.next!!
        }
        x = x.next!!
        val result = mutableListOf<Int>()
        while (x.value != 1) {
            result.add(x.value)
            x = x.next!!
        }
        return result.joinToString("")
    }

    fun getResult2(): BigInteger {
        var x = current
        while (x.value != 1) {
            x = x.next!!
        }
        val i1 = x.next!!.value
        val i2 = x.next!!.next!!.value
        return i1.toBigInteger().multiply(i2.toBigInteger())
    }

}

private fun parseInput(input: String): Cup {
    val first = Cup(input[0].toString().toInt(), null)
    var prev = first
    var counter = 1
    (1 until input.length).forEach {
        val next = Cup(input[it].toString().toInt(), null)
        prev.next = next
        prev = next
        counter++
    }
    var nextVal = input.map { it.toString().toInt() }.maxOrNull()!!
    while (counter < 1_000_000) {
        val next = Cup(++nextVal, null)
        prev.next = next
        prev = next
        counter++
    }
    prev.next = first
    return first
}

fun main() {
//    val input = "389125467"
    val input = "487912365"
    val moves = 10_000_000

    val cups = Cups(parseInput(input))

//    println(cups.toList().size)
//    println(cups.toList().subList(0, 20))

    (1..moves).forEach { i ->
//        println("\n----- Step $i")
//        println(cups.toList())

        val c1 = cups.pickNextToCurrent()
        val c2 = cups.pickNextToCurrent()
        val c3 = cups.pickNextToCurrent()
//        println("$c1, $c2, $c3")

        val destination = cups.findDestination()
//        println("destination: ${destination.value}")
        cups.insertAfter(destination, c3)
        cups.insertAfter(destination, c2)
        cups.insertAfter(destination, c1)

        cups.moveCurrentByOne()
    }

    println("FINAL")
//    println(cups.toList())
    println(cups.getResult2())
}

//-- move 1 --
//cups: (3) 8  9  1  2  5  4  6  7
//pick up: 8, 9, 1
//destination: 2
//
//-- move 2 --
//cups:  3 (2) 8  9  1  5  4  6  7
//pick up: 8, 9, 1
//destination: 7
//
//-- move 3 --
//cups:  3  2 (5) 4  6  7  8  9  1
//pick up: 4, 6, 7
//destination: 3
//
//-- move 4 --
//cups:  7  2  5 (8) 9  1  3  4  6
//pick up: 9, 1, 3
//destination: 7
//
//-- move 5 --
//cups:  3  2  5  8 (4) 6  7  9  1
//pick up: 6, 7, 9
//destination: 3
//
//-- move 6 --
//cups:  9  2  5  8  4 (1) 3  6  7
//pick up: 3, 6, 7
//destination: 9
//
//-- move 7 --
//cups:  7  2  5  8  4  1 (9) 3  6
//pick up: 3, 6, 7
//destination: 8
//
//-- move 8 --
//cups:  8  3  6  7  4  1  9 (2) 5
//pick up: 5, 8, 3
//destination: 1
//
//-- move 9 --
//cups:  7  4  1  5  8  3  9  2 (6)
//pick up: 7, 4, 1
//destination: 5
//
//-- move 10 --
//cups: (5) 7  4  1  8  3  9  2  6
//pick up: 7, 4, 1
//destination: 3
//
//-- final --
//cups:  5 (8) 3  7  4  1  9  2  6
