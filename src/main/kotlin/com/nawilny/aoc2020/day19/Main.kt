package com.nawilny.aoc2020.day19

import com.nawilny.aoc2020.common.Input

private sealed class Rule

private data class SimpleRule(val c: Char) : Rule()
private data class ReferencedRule(val rules: List<Int>) : Rule()
private data class OrRule(val subRules: List<Rule>) : Rule()

private val rules = mutableMapOf<Int, Rule>()

private fun parseRule(ruleStr: String): Rule {
    return when {
        ruleStr.startsWith("\"") -> SimpleRule(ruleStr[1])
        ruleStr.contains('|') -> OrRule(ruleStr.split('|').map { parseRule(it.trim()) })
        else -> ReferencedRule(ruleStr.split(' ').map { it.trim().toInt() })
    }
}

private fun followsRule(message: String, start: Int, rule: Rule): List<Int> {
    if (start >= message.length) {
        return listOf()
    }
    when (rule) {
        is SimpleRule -> {
            return if (message[start] == rule.c) listOf(start + 1) else listOf()
        }
        is ReferencedRule -> {
            var pointers = listOf(start)
            rule.rules.map { rules[it]!! }.forEach { currentRule ->
                pointers = pointers.flatMap { followsRule(message, it, currentRule) }
            }
            return pointers
        }
        is OrRule -> {
            return rule.subRules
                    .flatMap { followsRule(message, start, it) }
        }
    }
}

fun main() {
    val lines = Input.readFileLines("day19", "input2.txt").filter { it.isNotBlank() }

    val messages = mutableListOf<String>()

    lines.forEach {
        if (it.contains(':')) {
            rules[it.split(":")[0].trim().toInt()] = parseRule(it.split(":")[1].trim())
        } else {
            messages.add(it.trim())
        }
    }

    println(rules)

    val zeroRule = rules[0]!!
    val count = messages.map { msg ->
        val res = followsRule(msg, 0, zeroRule)
        res.any { it == msg.length }
    }.filter { it }.count()

    println(count)
}
