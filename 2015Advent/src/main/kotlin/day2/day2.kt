package day2

import java.io.File

fun main() {
    val boxes = File("src\\main\\kotlin\\day2\\day2.txt")
        .readLines()
        .map { stringDimension ->
            val (length, width, height) = stringDimension.split("x").map {it.toInt()}
            Present(length, width, height)
        }

    val part1Solution = boxes.sumOf { currentBox ->
        val areaComponents = currentBox.surfaceAreaComponents()
        val surfaceArea = areaComponents.surfaceArea()
        val slack = areaComponents.min()
        surfaceArea + slack
    }

    val part2Solution = boxes.sumOf { currentBox ->
        currentBox.volume() + currentBox.smallerPerimeter()
    }

    println("Part1Solution: $part1Solution")
    println("Part2Solution: $part2Solution")
}



data class Present(val length: Int, val width: Int, val height: Int)
typealias AreaComponents = Triple<Int, Int, Int>

// Part1 Utils
fun Present.surfaceAreaComponents() = AreaComponents(length*width, width*height, height*length)

fun AreaComponents.min(): Int = first.coerceAtMost(second.coerceAtMost(third))

fun AreaComponents.surfaceArea() = 2*(first + second + third)

// Part2 Utils
fun Present.volume() = length * height * width

fun Present.smallerPerimeter() =
    listOf(length, height, width).sorted().take(2).sumOf { minComponent ->  2*minComponent }

