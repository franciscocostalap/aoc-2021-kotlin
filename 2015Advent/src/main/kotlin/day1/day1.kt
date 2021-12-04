package day1

import getFile

fun main(){
    val input = getFile(1).readText().trim()
    val part1Solution = solution(input)
    val part2Solution = solution(input, -1)
    println("Part1: $part1Solution")
    println("Part2: $part2Solution")

}

private fun solution(input: String, stopFloor:Int?=null):Int{
    return input.foldIndexed(0){idx, acc, char ->
        if(acc == stopFloor) return idx
        acc + if(char == '(') 1 else if(char == ')') -1 else 0
    }
}