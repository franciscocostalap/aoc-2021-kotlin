package day3

import getFile

data class Direction(val dx: Int, val dy: Int)

fun Direction(directionChar: Char) =
    when(directionChar){
        '^' -> Direction(0, -1)
        '>' -> Direction(1, 0)
        '<' -> Direction(-1, 0)
        'v' -> Direction(0, 1)
        else -> error("Invalid direction input.")
    }

operator fun SantaState.plus(dir: Direction) = copy(x=x + dir.dx,y=y+dir.dy)

data class SantaState(val x: Int, val y: Int)

fun main() {
    val directions = getFile(3)
        .readText().map { Direction(it) }
    // Part1
    val santa = SantaState(0, 0)
    val part1Solution = santa.deliverPresents(directions).size

    // Part2
    val (santaDirections, robotDirections) = directions.partitionIndexed{idx, _ -> idx % 2 == 0 }
    val robotSanta = santa.copy()
    val part2Solution = santa.deliverPresents(santaDirections)
        .union(robotSanta.deliverPresents(robotDirections)).size

    println("Part1: $part1Solution")
    println("Part2: $part2Solution")

}

fun SantaState.deliverPresents(directions: List<Direction>): Set<SantaState>{
    val housesVisited = mutableSetOf(this)
    directions.fold(this){santaState, direction ->
        val newState = santaState + direction
        housesVisited.add(newState)
        newState
    }
    return housesVisited
}

inline fun <T> List<T>.partitionIndexed(predicate: (Int, T) -> Boolean): Pair<List<T>, List<T>> {
    val first = ArrayList<T>()
    val second = ArrayList<T>()
    for (i in this.indices) {
        val element = this[i]
        if (predicate(i, element)) {
            first.add(element)
        } else {
            second.add(element)
        }
    }
    return Pair(first, second)
}