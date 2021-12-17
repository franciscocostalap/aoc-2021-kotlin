package day09

import getFile
import getTest
import checkTest
import part1
import part2
import java.util.*

private const val TEST = true
private const val DAY = 9

typealias HeightMatrix = List<List<Pos>>
typealias Basin = Set<Pos>

data class Pos(val line: Int, val column:Int, val height: Int)

fun main(){
    val heightMatrix: HeightMatrix = (if(TEST) getTest(DAY) else getFile(DAY))
        .readText().split("\n").mapIndexed { line, lineString ->
            lineString.mapIndexed{ col, digit -> Pos(line, col, digit.toString().toInt()) }
        }

    // Your code
    val lowPoints = heightMatrix.flatMap { line ->
        line.filter { pos ->  pos.isLowPoint(heightMatrix)}
    }
    val part1 = lowPoints.sumOf { it.height+1 }

    val distinctBasins = lowPoints.map { it.findBasin(heightMatrix) }.distinct()
    val part2 = distinctBasins
        .map { it.size }
        .sortedByDescending{it}
        .take(3)
        .reduce{acc, basinSize -> acc*basinSize}


    if(TEST){
        checkTest(expected=15, actual=part1, "part1") // part1
        checkTest(expected=1134, actual=part2, "part2") // part2
    }
    part1(part1)
    part2(part2)
}

fun Pos.getNeighbours(heightMatrix: HeightMatrix): List<Pos>{
    val top = heightMatrix.getOrNull(line-1, column)
    val bottom = heightMatrix.getOrNull(line+1, column)
    val left = heightMatrix.getOrNull(line, column-1)
    val right = heightMatrix.getOrNull(line, column+1)
    return listOfNotNull(top, left, right, bottom)
}

fun Pos.isLowPoint(heightMatrix: HeightMatrix) = getNeighbours(heightMatrix).all { neigh -> neigh.height > this.height }

fun HeightMatrix.getOrNull(line: Int, column: Int): Pos?{
    val lineValidIdxs = indices
    val colValidIdxs = first().indices
    return if(line in lineValidIdxs && column in colValidIdxs)
        this[line][column]
    else
        null
}

fun Pos.findBasin(heightMatrix: HeightMatrix): Basin{ // Breadth first search
    val seen = mutableSetOf<Pos>()
    val frontier = LinkedList<Pos>()
    val basin = mutableSetOf<Pos>()
    frontier.add(this)
    while (frontier.isNotEmpty()){
        val currentPos = frontier.poll()
        if(currentPos.height != 9){
            val neighbours = currentPos.getNeighbours(heightMatrix)
            frontier.addAll(neighbours.filter{it !in seen})
            seen.addAll(neighbours)
            basin.add(currentPos)
        }
    }
    return basin
}