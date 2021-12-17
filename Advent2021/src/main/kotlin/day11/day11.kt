package day11

import getFile
import getTest
import checkTest
import combinations
import part1
import part2
import java.util.*

private const val TEST = false
private const val DAY = 11

typealias Grid = List<List<OctopusLevel>>
private fun Grid.print(step: Int){
    val red = "\u001b[31m"
    val reset = "\u001b[0m"
    println("After Step: $step")
    println(
        joinToString("\n") { line ->
            line.joinToString(" "){
                if(it.level == 0)
                    red + 0 + reset
                else it.level.toString()

            }
        }
    )
    println("----------------------------------------------")
}

data class OctopusLevel(val pos: Position, val level: Int)
data class Position(val line: Int, val column: Int)

fun main() {
    val input = (if (TEST) getTest(DAY) else getFile(DAY)).readLines()
    val grid = input.buildGrid()
    val steps = 100
    val part1Result = (1..steps).fold(StepResult(0, grid)){ stepAccumulator, step ->
        val newGrid = stepAccumulator.newGrid.applyStep()
        newGrid.print(step)
        StepResult(flashCount=stepAccumulator.flashCount+
                newGrid.sumOf {line -> line.count { it.level == 0 } },
            newGrid
        )
    }

    val part1 = part1Result.flashCount
    var step = 1
    var gridAcc = grid
    while(true){
        gridAcc = gridAcc.applyStep()
        gridAcc.print(step)
        if(gridAcc.all { line -> line.all { it.level == 0 } })
            break
        step++
    }
    val part2 = step


    if (TEST) {
        checkTest(expected = 1656, actual = part1, "part1") // part1
        checkTest(expected = 195, actual = part2, "part2") // part2
    }
    part1(part1)
    part2(part2)
}

private fun List<String>.buildGrid(): Grid{
    return mapIndexed() { lineIdx, line ->
        line.mapIndexed { colIdx, level ->
            OctopusLevel(Position(lineIdx, colIdx), level.toString().toInt())
        }
    }
}

private fun OctopusLevel.getNeighbours(grid: Grid): Set<Position>{
    val topLine = pos.line-1
    val leftColumn = pos.column-1
    val bottomLine = pos.line+1
    val rightColumn = pos.column+1
    val columns = listOf(leftColumn, rightColumn, pos.column)
    val neighbours = listOf(topLine, bottomLine, pos.line).combinations(columns){ line, column ->
        grid.getOrNull(line, column)?.pos
    }.filterNotNullTo(mutableSetOf())
    return neighbours
}

private fun Grid.getOrNull(line: Int, column: Int): OctopusLevel?{
    return if(line in indices && column in first().indices)
        this[line][column]
    else
        null
}

private fun Grid.applyStep(): Grid{
    val increasedByOne: Grid = map { line ->
        line.map { octopus ->
            octopus.copy(level=octopus.level+1)
        }
    }
    val aboutToFlash = increasedByOne.flatMap { line -> line.filter { it.level > 9 } }
    return aboutToFlash.fold(increasedByOne){ accGrid, octopus ->
        if(accGrid[octopus.pos.line][octopus.pos.column].level != 0) {
            accGrid.makeFlash(octopus)
        }
        else accGrid
    }
}

data class StepResult(val flashCount: Int, val newGrid: Grid)

private fun Grid.makeFlash(octopus: OctopusLevel): Grid{
    val localMutableGrid = this.map { it.toMutableList() }.toMutableList()
    val alreadySeen = mutableSetOf<Position>()
    val queue = LinkedList<OctopusLevel>()
    queue.add(octopus)
    while(queue.isNotEmpty()){
        val currentOctopus = queue.poll()
        val neighbours = currentOctopus
            .getNeighbours(localMutableGrid)
            .filter {localMutableGrid[it.line][it.column].level != 0}

        neighbours.forEach { neigh ->
            val elem = localMutableGrid[neigh.line][neigh.column]
            localMutableGrid[neigh.line][neigh.column] = elem.copy(level=elem.level+1)
        }

        localMutableGrid[currentOctopus.pos.line][currentOctopus.pos.column] = currentOctopus.copy(level=0)
        val toFlashYet = localMutableGrid.flatMap{line ->
            line.filter { it.pos in neighbours && it.pos !in alreadySeen && it.level > 9 }
        }

        queue.addAll(toFlashYet)
        alreadySeen.addAll(toFlashYet.map { it.pos })
        alreadySeen.add(currentOctopus.pos)
        //Thread.sleep(500)
        localMutableGrid.print(0)
    }
    return localMutableGrid
}



