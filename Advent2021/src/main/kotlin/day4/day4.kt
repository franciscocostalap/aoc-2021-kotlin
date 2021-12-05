package day4

import getFile

private val n: String = System.lineSeparator()
private const val SIDE_SIZE = 5

data class BingoBoard(val board: List<List<BingoNumber>>)

data class Game(val boards: List<BingoBoard>, val winners: List<BingoBoard>)

data class BingoNumber(val value: Int, val marked: Boolean)

fun BingoBoard(lines: List<String>): BingoBoard{
    val bingoBoard = lines.map {numberLine ->
        numberLine.split(" ")
        .mapNotNull {numberString ->
            val value = numberString.toIntOrNull()
            value?.let {checkedValue -> BingoNumber(checkedValue, false) }
        }
    }
    return BingoBoard(bingoBoard)
}

fun main(){
    val (numbersDrawn, boards) = getFile(4).readText().parseInput()
    var game = Game(boards, emptyList())
    val winnersPairs = numbersDrawn.mapNotNull{ numberDrawn ->
        val previousWinnerSize = game.winners.size
        game = game.makePlay(numberDrawn)
        val winners = game.winners
        if(winners.size > previousWinnerSize) numberDrawn to winners.last()
        else null
    }
    fun solution(pair: Pair<Int, BingoBoard>): Int = pair.second.getUnmarked().sum() * pair.first
    val part1Solution = solution(winnersPairs.first())
    val part2Solution = solution(winnersPairs.last())
    println("Part1: $part1Solution")
    println("Part2: $part2Solution")
}

fun Game.makePlay(number: Int): Game{
    val newBoards = boards.map{ it.mark(number) }
    val (winnersFound, stillPlaying) = newBoards.partition(BingoBoard::hasWon)
    return Game(stillPlaying, this.winners+winnersFound)
}

fun BingoBoard.mark(number: Int): BingoBoard = BingoBoard(
    board.map { line -> line.map { bingoNumber ->
        if(number == bingoNumber.value) bingoNumber.copy(marked = true) else bingoNumber
    }}
)

fun BingoBoard.hasWon(): Boolean{
    val lineCondition = board.any { line ->  line.all { bingoNumber -> bingoNumber.marked } }
    val columnCondition = (0 until SIDE_SIZE).any { column ->
        board.count { line -> line[column].marked } == SIDE_SIZE
    }
    return lineCondition || columnCondition
}

fun BingoBoard.getUnmarked(): List<BingoNumber> = board.flatten().filterNot{it.marked}

fun List<BingoNumber>.sum() = sumOf { it.value }

fun String.parseInput(): Pair<List<Int>, List<BingoBoard>>{
    val components = split("$n$n")
    val numbersDrawn = components.first().split(",").map(String::toInt)
    val boardsAsListsOfStrings = components.drop(1).map { boardString -> boardString.split(n) }
    val bingoBoards = boardsAsListsOfStrings.map(::BingoBoard)
    return Pair(numbersDrawn, bingoBoards)
}