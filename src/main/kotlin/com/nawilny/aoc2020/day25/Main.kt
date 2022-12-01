package com.nawilny.aoc2020.day25

import java.math.BigInteger

private val DIVISOR = 20201227.toBigInteger()

private fun findLoops(subjectNumber: Int, publicKey: Int): Int {
    val subjectNumberBI = subjectNumber.toBigInteger()
    val pk = publicKey.toBigInteger()
    var loops = 1
    var current = subjectNumberBI
    while (current != pk) {
        current *= subjectNumberBI
        current %= DIVISOR
        loops++
    }
    return loops
}

private fun encrypt(subjectNumber: Int, loops: Int): BigInteger {
    val subjectNumberBI = subjectNumber.toBigInteger()
    return (1 until loops).fold(subjectNumber.toBigInteger()) { sn, _ ->
        (sn * subjectNumberBI) % DIVISOR
    }
}

fun main() {

//    val pk1 = 5764801
//    val pk2 = 17807724

    val pk1 = 6270530
    val pk2 = 14540258

    val l1 = findLoops(7, pk1)
    val l2 = findLoops(7, pk2)

    println(l1)
    println(l2)

    println(encrypt(pk2, l1))
    println(encrypt(pk1, l2))

    // 6270530 -> 397860
//    14540258 -> 16774995
}
