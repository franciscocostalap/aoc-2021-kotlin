package day6

import java.io.File


fun main(){
    val commands = File("src\\main\\kotlin\\day6\\day6.txt")
        .readLines().map{ Command(it)}
    //val command = Command(CommandType.ON, Position(0, 0), Position(1, 1))
    val matrix = LightMatrix()
    matrix.print()
    commands.forEach{cmd -> cmd.execute(matrix)}
    matrix.print()
    val matrixFlattened = matrix.flatten()
    val part1Solution = matrixFlattened.count { light -> light.state == LightState.ON }
    val part2Solution = matrixFlattened.sumOf { light -> light.brightness }
    println("Part1: $part1Solution")
    println("Part2: $part2Solution")
}