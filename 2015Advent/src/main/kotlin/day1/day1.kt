package day1

import java.io.File


private const val INPUT_PATH = "src\\main\\kotlin\\day1\\day1.txt"

fun main(){
    val input = File(INPUT_PATH).readText().trim()
    val part1Solution = solution(input)
    val part2Solution = solution(input, -1)
    println("Part1: $part1Solution")
    println("Part2: $part2Solution")

}

fun solution(input: String, stopFloor:Int?=null):Int{
    return input.foldIndexed(0){idx, acc, char ->
        if(acc == stopFloor) return idx
        acc + if(char == '(') 1 else if(char == ')') -1 else 0
    }
}