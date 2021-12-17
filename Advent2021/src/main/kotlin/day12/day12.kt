package day12

import getFile
import getTest
import checkTest
import part1
import part2
import kotlin.collections.HashMap

private const val TEST = false
private const val DAY = 12


typealias Graph = Map<String, Neighbours>
typealias Neighbours = Set<String>

fun main() {
    val graph: Graph = (if (TEST) getTest(DAY) else getFile(DAY))
        .readLines()
        .parseEdges()

    // Your code
    val part1: Int = graph.distinctPaths("start", visited= mutableSetOf())


    if (TEST) {
        checkTest(expected = 10, actual = part1, "part1") // part1
        checkTest(expected = 0, actual = 0, "part2") // part2
    }
    part1(part1)
    part2(0)
}

private fun Graph.distinctPaths(
    from: String,
    to: String = "end",
    visited: MutableSet<String>
    )
: Int {

    // If reached final node return current path count incremented by one
    if(from == to) return 1

    // If the cave is small add to visited caves so it is not visited twice
    if (from.isSmallCave()) visited.add(from)

    // For each neighbour find all the paths found in this traversal
    val neighboursNotVisited = this[from]!!.filter { it !in visited }
    val pathsFound = neighboursNotVisited.fold(0) { pathsAcc, neigh ->
        if(neigh !in visited)
            pathsAcc + this.distinctPaths(neigh, visited = visited)
        else 0
    }

    // Remove the cave from the visited if it's there to search for other paths
    visited.remove(from)

    return pathsFound
}

private fun List<String>.parseEdges(): Graph {
    val localMutableGraph = HashMap<String, Set<String>>()
    this.forEach { edge ->
        val (id, idAdjacent) = edge.split("-")
        localMutableGraph[id] = localMutableGraph.getOrDefault(id, emptySet()) + idAdjacent
        localMutableGraph[idAdjacent] = localMutableGraph.getOrDefault(idAdjacent, emptySet()) + id
    }
    return localMutableGraph
}

private fun String.isSmallCave() = all { it.isLowerCase() }
