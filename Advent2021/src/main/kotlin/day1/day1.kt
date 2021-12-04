package day1

import getFile
import mapLines


fun main(){
    val depths = getFile(1).mapLines { it.toInt() }

    val part1Solution: Int = depths.countIncreases()
    val triplesSum = depths.windowed(3){ tripleList -> tripleList.sum() }
    val part2Solution = triplesSum.countIncreases()

    println("Part1: $part1Solution")
    println("Part2: $part2Solution")
}

fun List<Int>.countIncreases() =
    zipWithNext().count { currentPair ->
        currentPair.second > currentPair.first
    }