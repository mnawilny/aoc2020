package com.nawilny.aoc2020.day08

import com.nawilny.aoc2020.common.Input

data class Instruction(val operation: Operation, val arg: Int) {

    enum class Operation {
        NOP, ACC, JMP
    }

    companion object {
        fun parse(s: String): Instruction {
            val parts = s.split(" ")
            return Instruction(Operation.valueOf(parts[0].trim().toUpperCase()), parts[1].trim().toInt())
        }
    }

}

data class Program(val instructions: List<Instruction>) {

    enum class State {
        RUNNING, FINISHED, LOOP
    }

    var accumulator = 0
    var pos = 0
    var visitedPositions = mutableSetOf<Int>()

    fun tick(): State {
        if (pos < 0 || pos >= instructions.size) {
            return State.FINISHED
        }
        if (visitedPositions.contains(pos)) {
            return State.LOOP
        }
        visitedPositions.add(pos)
        val instruction = instructions[pos]
        when (instruction.operation) {
            Instruction.Operation.NOP -> pos++
            Instruction.Operation.ACC -> {
                accumulator += instruction.arg
                pos++
            }
            Instruction.Operation.JMP -> pos += instruction.arg
        }
        return State.RUNNING
    }
}

fun main() {
    val instructions = Input.readFileLines("day08", "input.txt")
            .filter { it.isNotBlank() }
            .map { Instruction.parse(it) }

    for (instruction in instructions.withIndex()) {
        val newOperation = when (instruction.value.operation) {
            Instruction.Operation.NOP -> Instruction.Operation.JMP
            Instruction.Operation.JMP -> Instruction.Operation.NOP
            Instruction.Operation.ACC -> Instruction.Operation.ACC
        }

        if (newOperation != instruction.value.operation) {
            val modifiedInstructions = instructions.subList(0, instruction.index)
                    .plus(Instruction(newOperation, instruction.value.arg))
                    .plus(instructions.subList(instruction.index + 1, instructions.size))

            val program = Program(modifiedInstructions)

            var state = Program.State.RUNNING
            while (state == Program.State.RUNNING) {
                state = program.tick()
            }

            if (state == Program.State.FINISHED) {
                println(program.accumulator)
            }
        }
    }

}
