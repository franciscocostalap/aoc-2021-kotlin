package day2

import getFile
import mapLines

private data class SubmarineState(val x: Int, val depth: Int, val aim: Int)

fun main() {
    val commands = getFile(2).mapLines{Command(it)}
    val part1SubState = commands.execute()
    val part2SubState = commands.execute(part2 = true)
    println("Part1: ${part1SubState.x * part1SubState.depth}")
    println("Part2: ${part2SubState.x * part2SubState.depth}")
}

private fun List<Command>.execute(part2: Boolean = false): SubmarineState =
    fold(SubmarineState(0, 0, 0)) { sub, command ->
        command.action(sub, part2)
    }

private sealed class Command(val action: (SubmarineState, Boolean) -> SubmarineState)

private fun Command(inputString: String): Command {
    val (direction, deltaString) = inputString.split(" ")
    val delta = deltaString.toIntOrNull() ?: error("Invalid delta.")
    return when (direction) {
        "forward" -> Forward(delta)
        "up" -> Up(delta)
        "down" -> Down(delta)
        else -> error("Invalid command.")
    }
}

private class Forward(delta: Int) : Command({ sub, part2 ->
    sub.copy(x = sub.x + delta, depth = if (!part2) sub.depth else sub.depth + sub.aim * delta)
})

private class Up(delta: Int) : Command({ sub, part2 ->
    if (!part2) sub.copy(depth = sub.depth - delta)
    else sub.copy(aim = sub.aim - delta)
})

private class Down(delta: Int) : Command({ sub, part2 ->
    if (!part2) sub.copy(depth = sub.depth + delta)
    else sub.copy(aim = sub.aim + delta)
})



