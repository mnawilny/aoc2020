package com.nawilny.aoc2020.day07

import com.nawilny.aoc2020.common.Input

data class Rules(val rules: Map<String, Rule>) {

    fun canContain(colour: String) = rules.filter { it.value.canContain(colour) }.map { it.key }.toSet()

    fun countBagsInside(color: String): Int {
        return (rules[color] ?: error("Color $color not found")).contains
                .map { countBagsInside(it.key) * it.value }
                .sum() + 1
    }

}

data class Rule(val colour: String, val contains: Map<String, Int>) {

    fun canContain(colour: String) = contains.containsKey(colour)

    companion object {
        fun parse(line: String): Rule {
            val parts = line.split("bags contain").map { it.trim() }
            val colour = parts[0]
            val contains = if (parts[1].startsWith("no other bags")) {
                mapOf()
            } else {
                parts[1].split(",").map { it.trim() }
                        .map { it.substringAfter(" ").substringBeforeLast(" ") to it.substringBefore(" ").toInt() }
                        .toMap()
            }
            return Rule(colour, contains)
        }
    }
}

fun main() {
    val rulesMap = Input.readFileLines("day07", "input.txt")
            .filter { it.isNotBlank() }
            .map { it.replace(".", "") }
            .map { Rule.parse(it) }
            .map { it.colour to it }
            .toMap()
    val rules = Rules(rulesMap)

    // part1
//    var resultBags = setOf<String>()
//    var nextResultsBag = rules.canContain("shiny gold")
//
//    while (resultBags != nextResultsBag) {
//        resultBags = nextResultsBag
//        resultBags.forEach {
//            nextResultsBag = nextResultsBag.plus(rules.canContain(it))
//        }
//    }
//    println(resultBags)
//    println(resultBags.size)

    println(rules.countBagsInside("shiny gold") - 1)
}
