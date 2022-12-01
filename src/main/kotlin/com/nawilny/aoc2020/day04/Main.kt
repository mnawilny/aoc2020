package com.nawilny.aoc2020.day04

import com.nawilny.aoc2020.common.Input

data class Document(
        val byr: String?,
        val iyr: String?,
        val eyr: String?,
        val hgt: String?,
        val hcl: String?,
        val ecl: String?,
        val pid: String?,
        val cid: String?
) {

    fun isValid() = isByrValid()
            && isIyrValid()
            && isEyrValid()
            && isHgtValid()
            && isHclValid()
            && isEclValid()
            && isPidValid()

    private fun isByrValid() = isNumberBetween(byr, IntRange(1920, 2002))

    private fun isIyrValid() = isNumberBetween(iyr, IntRange(2010, 2020))

    private fun isEyrValid() = isNumberBetween(eyr, IntRange(2020, 2030))

    private fun isHgtValid(): Boolean {
        if (hgt.isNullOrBlank()) {
            return false
        }
        return when {
            hgt.endsWith("cm") -> isNumberBetween(hgt.substring(0, hgt.length - 2), IntRange(150, 193))
            hgt.endsWith("in") -> isNumberBetween(hgt.substring(0, hgt.length - 2), IntRange(59, 76))
            else -> false
        }
    }

    private fun isHclValid(): Boolean {
        return !hcl.isNullOrBlank() && hcl.matches(Regex("#[0-9a-f]{6}"))
    }

    private fun isEclValid(): Boolean {
        return !ecl.isNullOrBlank() && setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(ecl)
    }

    private fun isPidValid(): Boolean {
        return !pid.isNullOrBlank() && pid.length == 9 && pid.toIntOrNull() != null
    }

    private fun isNumberBetween(value: String?, range: IntRange): Boolean {
        val number = value?.toIntOrNull()
        return number != null && range.contains(number)
    }

}

fun parseLine(line: String): Document {
    val values = line.split(" ")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { it.split(":") }
            .map { it[0] to it[1] }
            .toMap()
    return Document(
            byr = values["byr"],
            iyr = values["iyr"],
            eyr = values["eyr"],
            hgt = values["hgt"],
            hcl = values["hcl"],
            ecl = values["ecl"],
            pid = values["pid"],
            cid = values["cid"]
    )
}

fun parse(lines: List<String>): List<Document> {
    var entryLine = ""
    val documents = mutableListOf<Document>()
    lines.forEach { line ->
        if (line.isBlank()) {
            if (entryLine.isNotBlank()) {
                documents.add(parseLine(entryLine))
                entryLine = ""
            }
        }
        entryLine += " $line"
    }
    if (entryLine.isNotBlank()) {
        documents.add(parseLine(entryLine))
    }
    return documents
}

fun main() {
    val lines = Input.readFileLines("day04", "input.txt")
    val documents = parse(lines)
    documents.forEach { println(it) }
    println(documents.filter { it.isValid() }.count())
}
