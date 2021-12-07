package day5

import getFile
import mapLines
import kotlin.math.abs
import kotlin.math.max

private typealias Grid = List<MutableList<Int>>

private fun Grid(width: Int, height: Int): Grid = List(height) { MutableList(width) { 0 } }

private data class Position(val x: Int, val y: Int)

private fun Position(string: String): Position {
    val (x, y) = string.split(",").map { it.toInt() }
    return Position(x, y)
}

private data class Dir(val dx: Int, val dy: Int)
private fun Dir.abs() = Dir(abs(dx), abs(dy))

private data class Line(val from: Position, val to: Position){
    val direction = Dir(to.x - from.x, to.y - from.y)
    val type = toLineType()
}

private fun Line.toLineType(): LineType{
    val direction = direction
    val absDir = direction.abs()
    return when {
        direction.dx != 0 && direction.dy == 0 -> LineType.HORIZONTAL
        direction.dx == 0 && direction.dy != 0 -> LineType.VERTICAL
        absDir.dx == absDir.dy -> LineType.DIAGONAL
        else -> error("Invalid line.")
    }
}

private enum class LineType{
    HORIZONTAL, VERTICAL, DIAGONAL
}

private fun Line(string: String): Line {
    // 0,9 -> 5,9
    val (firstPos, secondPos) = string.split(" -> ")
    return Line(Position(firstPos), Position(secondPos))
}

fun main() {
    val ventLines = getFile(5).mapLines(::Line)

    val gridWidth = ventLines.maxOf { max(it.from.x, it.to.x) }
    val gridHeight = ventLines.maxOf { max(it.from.y, it.to.y) }

    val grid = Grid(gridWidth + 1, gridHeight + 1)

    val (linearLines, diagonalLines) = ventLines.partition {
        it.type == LineType.HORIZONTAL || it.type == LineType.VERTICAL
    }

    grid mark linearLines
    val part1Solution = solution(grid)

    grid mark diagonalLines
    val part2Solution = solution(grid)

    println("Part1Solution: $part1Solution")
    println("Part2Solution: $part2Solution")
}

fun solution(grid: Grid): Int{
    return grid.sumOf { line -> line.count { it >= 2 } }
}

private infix fun Grid.mark(lines: List<Line>) = lines.forEach { markVector(it) }

private fun Grid.markVector(vector: Line) {
    val from = vector.from
    val to = vector.to
    val xFactor = if(from.x > to.x) -1 else if(from.x == to.x) 0 else 1
    val yFactor = if(from.y > to.y) -1 else if(from.y == to.y) 0 else 1
    val absDir = vector.direction.abs()
    var absX = 0
    var absY = 0
    while(absX <= absDir.dx || absY <= absDir.dy){
        val currY = from.y+ absY*yFactor
        val currX = from.x+ absX*xFactor
        this[currY][currX]++
        absX++
        absY++
    }
}
