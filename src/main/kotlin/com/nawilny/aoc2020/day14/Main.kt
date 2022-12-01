package com.nawilny.aoc2020.day14

import com.nawilny.aoc2020.common.Input

class Memory1 {

    private val mem = mutableMapOf<Long, Long>()
    private var mask: Map<Int, Int> = mutableMapOf()

    fun setMask(maskStr: String) {
        mask = maskStr.withIndex()
                .filter { it.value.isDigit() }
                .map { Pair(35 - it.index, it.value.toString().toInt()) }
                .toMap()
    }

    fun set(address: Long, value: Long) {
        mem[address] = applyMask(value)
    }

    private fun applyMask(n: Long): Long {
        var number = n
        val bits = mutableListOf<Int>()
        (0..35).forEach {
            if (mask.containsKey(it)) {
                bits.add(mask[it]!!)
            } else {
                bits.add((number % 2).toInt())
            }
            number /= 2
        }
        var pow2 = 1L
        var result = 0L
        bits.forEach {
            result += it * pow2
            pow2 *= 2
        }
        return result
    }

    fun sum() = mem.values.sum()
}

class Memory2 {

    private val mem = mutableMapOf<Long, Long>()
    private var mask: String? = null

    fun setMask(maskStr: String) {
        this.mask = maskStr
    }

    fun set(address: Long, value: Long) {
        generateAddresses(address).forEach {
            mem[it] = value
        }
    }

    private fun generateAddresses(address: Long): Set<Long> {
        if (mask == null) {
            return setOf(address)
        }
        var number = address
        val bits = mutableSetOf(mutableListOf<Int>())
        (0..35).forEach { i ->
            when (val maskValue = mask!![35-i]) {
                '0' -> bits.forEach { it.add((number % 2).toInt()) }
                '1' -> bits.forEach { it.add(1) }
                'X' -> {
                    val toAdd = mutableSetOf<MutableList<Int>>()
                    bits.forEach {
                        val copy = mutableListOf<Int>()
                        copy.addAll(it)
                        copy.add(0)
                        toAdd.add(copy)
                        it.add(1)
                    }
                    bits.addAll(toAdd)
                }
                else -> error("unsupported mask bit '$maskValue'")
            }
            number /= 2
        }

        return bits.fold(mutableSetOf()) { acc, b ->
            acc.add(toLong(b))
            acc
        }
    }

    private fun toLong(bits :List<Int>) :Long {
        var pow2 = 1L
        var result = 0L
        bits.forEach {
            result += it * pow2
            pow2 *= 2
        }
//        println(bits)
//        println(result)
        return result
    }

    fun sum() = mem.values.sum()
}

fun main() {
    val setMemRegexp = """mem\[(\d+)] = (\d+)""".toRegex()

    val commands = Input.readFileLines("day14", "input.txt")
            .filter { it.isNotBlank() }

    val memory = Memory2()

    commands.forEach {
        when {
            it.startsWith("mask = ") -> memory.setMask(it.substring(7))
            setMemRegexp.matches(it) -> {
                val values = setMemRegexp.matchEntire(it)!!.groupValues
                memory.set(values[1].toLong(), values[2].toLong())
            }
            else -> error("Unknown command: $it")
        }
    }

    println("--------")
    println(memory.sum())
}
