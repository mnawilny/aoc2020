package com.nawilny.aoc2020.day15

fun main() {
    val input = "16,12,1,0,15,7,11"

    val spokenNumbers = mutableMapOf<Int, Int>()

    var turn = 1

    var lastNumber = 0
    input.split(",").map { it.toInt() }.forEach {
        if (turn > 1) {
            spokenNumbers[lastNumber] = turn - 1
        }
        lastNumber = it
        turn++
//        println(lastNumber)
    }

    while (turn <= 30000000) {
        val nextNumber = if (spokenNumbers.containsKey(lastNumber)) {
            turn - spokenNumbers[lastNumber]!! - 1
        } else {
            0
        }
        spokenNumbers[lastNumber] = turn - 1
        lastNumber = nextNumber
//        println(nextNumber)
        turn++
    }
    println("------")
    println(lastNumber)
}
