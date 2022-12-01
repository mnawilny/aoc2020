package com.nawilny.aoc2020.day13

import java.math.BigInteger

fun findTimestamp(busses: Map<BigInteger, BigInteger>): BigInteger {
    var i = BigInteger.ZERO
    while (true) {
        if (busses.all { (i.plus(it.value)).mod(it.key) == BigInteger.ZERO }) {
            return i
        }
        i = i.plus(BigInteger.ONE)
        if (i.mod(BigInteger.valueOf(1000000000)) == BigInteger.ZERO) {
            println(i)
        }
    }
}

fun lowestDay(bus: Int, index: Int): Int {
    var res = bus - index
    while (res < 0) {
        res += bus
    }
    return res
}

fun aa(bus1: Pair<BigInteger, BigInteger>, bus2: Pair<BigInteger, BigInteger>): Pair<BigInteger, BigInteger> {
    var res = bus1.second
    while (true) {
        if (res.mod(bus2.first) == bus2.second) {
            return Pair(bus1.first.multiply(bus2.first), res)
        }
        res = res.plus(bus1.first)
    }
}

fun main() {

//    val input = "7,13,x,x,59,x,31,19" // 1068781
//    val input = "17,x,13,19" // 3417
//    val input = "67,7,59,61" // 754018
//    val input = "67,x,7,59,61" // 779210
//    val input = "67,7,x,59,61" // 1261476
//    val input = "1789,37,47,1889" // 1202161486
    val input = "17,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,643,x,x,x,x,x,x,x,23,x,x,x,x,13,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,29,x,433,x,x,x,x,x,37,x,x,x,x,x,x,x,x,x,x,x,x,19"

//    val input = "5,7"
//    val input = "7,13,19"

    val inputList = input.split(",").withIndex().mapNotNull {
        if (it.value == "x") {
            null
        } else {
            Pair(it.value.toBigInteger(), lowestDay(it.value.toInt(), it.index).toBigInteger())
        }
    }

    var result: Pair<BigInteger, BigInteger>? = null
    inputList.forEach {
        result = if (result == null) {
            it
        } else {
            aa(result!!, it)
        }
    }
    println(result!!.second)

//    val a1 = aa(inputList[0], inputList[1])
//    val a2 = aa(a1, inputList[2])
//    println(a2)

//    (1..10000).forEach { i ->
//        if (inputMap.all { (i.toBigInteger().plus(it.value)).mod(it.key) == BigInteger.ZERO }) {
//            println(i)
//        }
//    }
//    println(findTimestamp(inputMap))
}
