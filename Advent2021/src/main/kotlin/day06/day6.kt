package day06

import getFile
import getTest

typealias Population = Map<Int, Long>

fun List<Int>.toPopulation(): Population =
     groupingBy { fishAge -> fishAge }
    .eachCount()
    .mapValues { (_, size) -> size.toLong() }

private const val DEBUG = false
private const val DAY = 6

fun main(){
    val input = if(DEBUG) getTest(DAY) else getFile(DAY)
    val lanternsPopulation = input.readText()
        .split(",")
        .map(String::toInt)
        .toPopulation()

    val part1Solution = (lanternsPopulation after 80).size()
    val part2Solution = (lanternsPopulation after 256).size()
    println("Part1: $part1Solution")
    println("Part2: $part2Solution")
}

fun Population.size(): Long = values.sum()

private infix fun Population.after(days: Int): Population{
    return (1..days).fold(this){ currentPopulation, _ ->
        currentPopulation.afterOneDay()
    }
}

private fun Population.afterOneDay(): Population {
    fun Population.getSizeOf(counter: Int) = getOrDefault(counter, 0)
    val auxMap = mapKeys { (daysToBirth, _) -> daysToBirth - 1 }.toMutableMap()
    val bornToday = auxMap.getSizeOf(-1)
    auxMap.remove(-1)
    auxMap[8] = bornToday
    auxMap[6] = auxMap.getOrDefault(6, 0) + bornToday
    return auxMap
}
