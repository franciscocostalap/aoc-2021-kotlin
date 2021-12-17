package day07

import getFile
import getTest
import part1
import part2
import kotlin.math.abs

private const val DEBUG = false
private const val DAY = 7
typealias Crabs = List<Int>

fun main(){
    val input = (if(DEBUG) getTest(DAY) else getFile(DAY)).readLines()[0]
    val crabs = input.split(",").map { it.toInt() }

    val part1 = crabs.getMinimumFuel {steps -> steps}
    val part2 = crabs.getMinimumFuel { steps -> (steps + 1) * steps / 2 }

    part1(part1)
    part2(part2)
}

fun Crabs.getMinimumFuel(fuel: (Int) -> (Int)): Int{
    val minCrab = minOf { it }
    val maxCrab = maxOf { it }
    return (minCrab..maxCrab).minOfOrNull { meetingPos ->
        this.sumOf { startingPos ->
            fuel(abs(meetingPos - startingPos))
        }
    } ?: error("No solution.")
}
