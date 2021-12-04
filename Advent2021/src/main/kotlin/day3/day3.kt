package day3

import getFile

val binaryNumbers = getFile(3).readLines()
val bitIndices = binaryNumbers.first().indices

fun main(){
    val gammaRateString = binaryNumbers.getFrequencyPairs().map { (oneCount, zeroCount) ->
        if(oneCount >= zeroCount) '1' else '0'
    }.stringed()
    val epsilonRate = gammaRateString.binaryInverted().toBinaryInt()

    val oxygenGeneratorRate = getRating(mostNLessCommon = true)
    val c02ScrubberRate = getRating(mostNLessCommon = false)

    val part1Solution = gammaRateString.toBinaryInt() * epsilonRate
    val part2Solution = c02ScrubberRate * oxygenGeneratorRate

    println("Part1: $part1Solution")
    println("Part2 : $part2Solution")
}

private fun String.binaryInverted() = map { if(it == '1') '0' else '1' }.stringed()

private fun String.toBinaryInt() = toInt(2)

private fun getRating(mostNLessCommon: Boolean): Int =
    bitIndices.fold(binaryNumbers){ listAccumulator, position ->
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


private fun List<Char>.stringed() = joinToString("")

private fun List<String>.getFrequencyPairs(): List<Pair<Int, Int>> =
    frequencyList('1').zip(frequencyList('0'))

private fun List<String>.getFrequencyPair(position: Int): Pair<Int, Int> =
    getFrequency(position, '1') to getFrequency(position, '0')

private fun List<String>.getFrequency(position: Int, digit: Char): Int =
    count{ string ->
        string[position] == digit
    }

private fun List<String>.frequencyList(digit:Char): List<Int> =
    bitIndices.map { position ->
        getFrequency(position, digit)
    }


