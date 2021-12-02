package day1

import mapLines
import java.io.File


fun main(){
    val depths = File("src\\main\\kotlin\\day1\\day1.txt")
        .mapLines { it.toInt() }

    val part1Solution: Int = depths.countIncreases()
    val triplesSum = depths.windowed(3, 1){ tripleList -> tripleList.sum() }
    val part2Solution = triplesSum.countIncreases()

    println("Part1: $part1Solution")
    println("Part2: $part2Solution")
}

fun List<Int>.countIncreases() =
    zipWithNext().count { currentPair ->
        currentPair.second > currentPair.first
    }