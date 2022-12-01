package com.nawilny.aoc2020.day21

import com.nawilny.aoc2020.common.Input

private data class FoodItem(val id: Int, val ingredients: Set<String>, val allergens: Set<String>)

private fun parseFoodItem(index: Int, line: String): FoodItem {
    val lineRegexp = """(.+) \(contains (.+)\)""".toRegex()
    val values = lineRegexp.matchEntire(line)!!.groupValues
    val ingredients = values[1].split(" ").filter { it.isNotBlank() }.toSet()
    val allergens = values[2].split(",").map { it.trim() }.filter { it.isNotBlank() }.toSet()
    return FoodItem(index, ingredients, allergens)
}

fun main() {
    val items = Input.readFileLines("day21", "input.txt")
            .withIndex()
            .filter { it.value.isNotBlank() }
            .map { parseFoodItem(it.index, it.value) }
    items.forEach { println(it) }

    val ingredients = items.flatMap { it.ingredients }.toSet()
    val possibleAllergens = items.flatMap { it.allergens }.distinct()
            .map { Pair(it, ingredients.toMutableSet()) }.toMap()
    println(possibleAllergens.keys)

    items.forEach { item ->
        item.allergens.forEach { allergen ->
            val possibleIngredients = possibleAllergens[allergen]!!
            possibleIngredients.removeAll { !item.ingredients.contains(it) }
        }
    }

    println(possibleAllergens)
//    sesame=[stj],
//    eggs=[kbmlt],
//    shellfish=[jvgnc],
//    nuts=[lpzgzmk],
//    peanuts=[ppj],
//    soy=[gxnr],
//    fish=[mrccxm],
//    wheat=[plrlg]

    val allergens = mapOf(
            "sesame" to "stj",
            "eggs" to "kbmlt",
            "shellfish" to "jvgnc",
            "nuts" to "lpzgzmk",
            "peanuts" to "ppj",
            "soy" to "gxnr",
            "fish" to "mrccxm",
            "wheat" to "plrlg"
    )

    val result1 = items.map { it.ingredients }.map { it.minus(allergens.values) }.map { it.size }.sum()
    println(result1)

    val result2 = allergens.map { Pair(it.key, it.value) }.sortedBy { it.first }.map { it.second }.joinToString(",")
    println(result2)
}
