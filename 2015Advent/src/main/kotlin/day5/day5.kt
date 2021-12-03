package day5

import java.io.File
import java.util.*


fun main(){
    val inputStrings = File("src\\main\\kotlin\\day5\\day5.txt")
        .readLines().map { it.trim() }

    val part1Solution = inputStrings.count(String::isNice1)
    val part2Solution = inputStrings.count(String::isNice2)
    println("Part1: $part1Solution")
    println("Part2: $part2Solution")
}


fun String.isNice1(): Boolean{
    val vowelsCondition = count { it in "aeiou" } >= 3
    val lettersInARowCondition: Boolean =
        windowed(2).any {letterLists -> letterLists.all { it == letterLists.first() } }
    val subStringsToIgnoreCondition = setOf("ab", "cd", "pq", "xy").all { it !in this }
    return vowelsCondition && lettersInARowCondition && subStringsToIgnoreCondition
}

fun String.isNice2(): Boolean{
    return """(?=.*(..).*\1)(?=.*(.).\2.*).*""".toRegex().matches(this)
}