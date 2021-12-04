package day6

import getFile
import mapLines

fun main(){
    val commands = getFile(6).mapLines{ Command(it)}
    val matrix = LightMatrix()
    commands.forEach{cmd -> cmd.execute(matrix)}
    val matrixFlattened = matrix.flatten()
    val part1Solution = matrixFlattened.count { light -> light.state == LightState.ON }
    val part2Solution = matrixFlattened.sumOf { light -> light.brightness }
    println("Part1: $part1Solution")
    println("Part2: $part2Solution")
}