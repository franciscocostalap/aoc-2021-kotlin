package day3

import java.io.File

val binaryNumbers = File("src/main/kotlin/day3/day3.txt")
        .readLines()
val bitIndices = binaryNumbers.first().indices

fun main(){
    val gammaRateString = binaryNumbers.getFrequencyPairs().map { (oneCount, zeroCount) ->
        if(oneCount >= zeroCount) '1' else '0'
    }.stringed()
    val epsilonRate = gammaRateString.binaryInverted().toBinaryInt()

    val oxygenGeneratorRate = getRating(true)
    val c02ScrubberRate = getRating(false)

    val part1Solution = gammaRateString.toBinaryInt() * epsilonRate
    val part2Solution = c02ScrubberRate * oxygenGeneratorRate

    println("Part1: $part1Solution")
    println("Part2 : $part2Solution")
}

fun String.binaryInverted() = map { if(it == '1') '0' else '1' }.stringed()

fun String.toBinaryInt() = toInt(2)

fun getRating(mostNLessCommon: Boolean): Int {
    return bitIndices.fold(binaryNumbers){ listAccumulator, position ->
        if(listAccumulator.size == 1) return@fold listAccumulator
        val currentPair = listAccumulator.getFrequencyPair(position)
        val mostOrLessCondition =
            if(mostNLessCommon)
                currentPair.first >= currentPair.second
            else
                currentPair.first < currentPair.second
        listAccumulator.filter { binaryString ->
            binaryString[position] ==
                    if(mostOrLessCondition)  '1'
                    else '0'
        }
    }.single().toBinaryInt()
}

fun List<Char>.stringed() = joinToString("")

fun List<String>.getFrequencyPairs(): List<Pair<Int, Int>> =
    frequencyList('1').zip(frequencyList('0'))

fun List<String>.getFrequencyPair(position: Int) =
    getFrequency(position, '1') to getFrequency(position, '0')

fun List<String>.getFrequency(position: Int, digit: Char) =
    count{ string ->
        string[position] == digit
    }

fun List<String>.frequencyList(digit:Char): List<Int>{
    return bitIndices.map { position ->
        getFrequency(position, digit)
    }
}

