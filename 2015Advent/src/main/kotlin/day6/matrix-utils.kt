package day6

typealias LightMatrix = MutableList<MutableList<Light>>

private const val MATRIX_SIZE = 1000

fun LightMatrix(): LightMatrix = MutableList(MATRIX_SIZE){
    MutableList(MATRIX_SIZE){Light(LightState.OFF, 0)}
}

data class Position(val line:Int, val column:Int)

fun Position(string: String): Position{
    val (line, column) = string.split(",").map(String::toInt)
    return Position(line, column)
}

enum class LightState{
    ON, OFF
}

data class Light(val state: LightState, val brightness: Int)

operator fun LightMatrix.get(position: Position): Light = this[position.line][position.column]

fun LightMatrix.print() = forEach(::println)
