package day2

import java.io.File
import mapLines

data class SubmarineState(val x: Int, val depth: Int, val aim: Int)

fun main() {
    val commands = File("src/main/kotlin/day2/day2.txt")
        .mapLines{Command(it)}
    val part1SubState = commands.execute()
    val part2SubState = commands.execute(part2 = true)
    println("Part1: ${part1SubState.x * part1SubState.depth}")
    println("Part2: ${part2SubState.x * part2SubState.depth}")
}

fun List<Command>.execute(part2: Boolean = false): SubmarineState =
    fold(SubmarineState(0, 0, 0)) { sub, command ->
        command.action(sub, part2)
    }

sealed class Command(val action: (SubmarineState, Boolean) -> SubmarineState)

fun Command(inputString: String): Command {
    val (direction, deltaString) = inputString.split(" ")
    val delta = deltaString.toIntOrNull() ?: error("Invalid delta.")
    return when (direction) {
        "forward" -> Forward(delta)
        "up" -> Up(delta)
        "down" -> Down(delta)
        else -> error("Invalid command.")
    }
}

class Forward(delta: Int) : Command({ sub, part2 ->
    sub.copy(x = sub.x + delta, depth = if (!part2) sub.depth else sub.depth + sub.aim * delta)
})

class Up(delta: Int) : Command({ sub, part2 ->
    if (!part2) sub.copy(depth = sub.depth - delta)
    else sub.copy(aim = sub.aim - delta)
})

class Down(delta: Int) : Command({ sub, part2 ->
    if (!part2) sub.copy(depth = sub.depth + delta)
    else sub.copy(aim = sub.aim + delta)
})



