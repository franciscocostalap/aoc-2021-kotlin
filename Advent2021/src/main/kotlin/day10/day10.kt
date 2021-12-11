package day10

import getFile
import getTest
import checkTest
import part1
import part2
import java.util.*

private const val TEST = false
private const val DAY = 10

private val bracketAssociation = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
)

private val openingBrackets = bracketAssociation.keys

fun main(){
    val input = (if(TEST) getTest(DAY) else getFile(DAY)).readLines()
    val analysisResults = input.map(String::validate)

    val pointsTablePart1 = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137,
    )
    val part1 = analysisResults.filterIsInstance<IllegalBracketResult>().sumOf {
        pointsTablePart1.getOrDefault(it.illegalBracket, 0)
    }

    val pointsTablePart2 = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4,
    )
    val incompleteLines = analysisResults.filterIsInstance<IncompleteLineResult>()
    val incompleteLinesScore = incompleteLines.map{it.getScore(pointsTablePart2)}
    val part2 = incompleteLinesScore.sorted()[incompleteLinesScore.size/2]

    if(TEST){
        checkTest(expected=26397, actual=part1, "part1") // part1
        checkTest(expected=288957, actual=part2, "part2") // part2
    }
    part1(part1)
    part2(part2)
}

sealed class Result
private data class IllegalBracketResult(val illegalBracket: Char): Result()
private data class IncompleteLineResult(val missingBrackets: Stack<Char>): Result()

fun String.validate(): Result{
    val localStack = Stack<Char>()
    for(bracket in this){
        if(bracket in openingBrackets){
            localStack.push(bracketAssociation[bracket])
        }else{
            if(localStack.isEmpty() || bracket != localStack.pop())
                return IllegalBracketResult(bracket)
        }
    }
    return IncompleteLineResult(localStack)
}

private fun IncompleteLineResult.getScore(pointsTable: Map<Char, Int>): Long =
    missingBrackets.reversed().fold(0L){ total, bracket ->
        total * 5 + pointsTable[bracket]!!
    }
