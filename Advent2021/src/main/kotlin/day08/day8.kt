package day08

import getFile
import getTest
import checkTest
import part1
import part2
import java.util.*

private const val TEST = true
private const val DAY = 8

typealias Length = Int


private fun lengthKnowledge(): Map<Length, Int>{
    return mapOf(
        2 to 1,
        3 to 7,
        4 to 4,
        7 to 8,
    )
}

private data class InputLine(val exclusiveSignals: List<SortedSet<Char>>, val outputValues: List<SortedSet<Char>>)

private fun InputLine(line: String): InputLine{
    val (exclusiveSignals, outputValues) =
        line.split(" | ").map { it.split(" ").map { signal -> signal.toSortedSet()  }}
    return InputLine(exclusiveSignals, outputValues)
}

fun main(){
    val input = (if(TEST) getTest(DAY) else getFile(DAY))
        .readLines()
        .map(::InputLine)

    val knowledge = lengthKnowledge()
    val part1 = input.sumOf { inputLine ->
        inputLine.outputValues.count {signalSet -> knowledge[signalSet.size] != null  }
    }

    val inferedSignals = input.map{ it.exclusiveSignals.infer() to it.outputValues }

    val part2 = inferedSignals.sumOf { pair ->
        val inferedWires = pair.first
        pair.second.joinToString("") { set ->
            inferedWires[set].toString()
        }.toInt()
    }

    if(TEST){
        checkTest(expected=26, actual=part1, "part1")
        checkTest(expected=61229, actual=part2, "part2")
    }
    part1(part1)
    part2(part2)
}

private fun List<SortedSet<Char>>.infer(): Map<SortedSet<Char>, Int>{
    val knowledge = mutableMapOf<Int, SortedSet<Char>>(
        1 to first{it.size == 2},
        4 to first{it.size == 4},
        7 to first{it.size == 3},
        8 to first{it.size == 7}
    )
    val size5 = this.filter { wireSet -> wireSet.size == 5 }
    val wires3 = size5.first { wireSet ->
        val minus = wireSet.filterTo(sortedSetOf()){it in wireSet && it !in knowledge[7]!!}
        minus.size == 2
    }
    val wires2 = size5.first { wireSet ->
        wireSet.intersect(knowledge[4]!!).size == 2
    }
    val wires5 = size5.first { !it.containsAll(wires2) && !it.containsAll(wires3) }

    val size6 = this.filter { wireSet -> wireSet.size == 6 }
    val wires9 = size6.first { wireSet->  wireSet.intersect(wires3).size == 5 }
    val wires6 = size6.first{wireSet ->
        wireSet.intersect(knowledge[7]!!).size == 2
    }
    val wires0 = size6.first{it != wires6 && it != wires9}

    return with(mutableMapOf<SortedSet<Char>, Int>()){
        put(value=0, key=wires0)
        put(knowledge[1]!!, 1)
        put(value=2, key=wires2)
        put(value=3, key=wires3)
        put(knowledge[4]!!, 4)
        put(value=5, key=wires5)
        put(value=6, key=wires6)
        put(knowledge[7]!!, 7)
        put(knowledge[8]!!, 8)
        put(value=9, key=wires9)
        return@with this
    }
}