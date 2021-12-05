package day6

import kotlin.math.max

data class Command(val type: CommandType, val from: Position, val to: Position)

enum class CommandType(val deltaBrightness: Int) {
    ON(1), OFF(-1), TOGGLE(2)
}

private fun CommandType.getState(matrix: LightMatrix, from: Position): LightState =
    when(this){
        CommandType.ON -> LightState.ON
        CommandType.OFF -> LightState.OFF
        CommandType.TOGGLE -> !matrix[from].state
    }


fun Command.execute(matrix: LightMatrix){
    (from.line..to.line).forEach{ line ->
        (from.column..to.column).forEach{ column ->
            val currentLight = matrix[line][column]
            matrix[line][column] = currentLight.copy(
                state = type.getState(matrix, Position(line, column)),
                brightness = max(0, currentLight.brightness + type.deltaBrightness)
            )
        }
    }
}

private val commandRegex =
    """(turn|toggle) (on|off)? ?([0-9]+,[0-9]+) through ([0-9]+,[0-9]+)""".toRegex()

fun Command(string: String): Command{
    val commandParts = commandRegex.find(string)?.destructured?.toList() ?: error("Invalid Input.")
    val onNOff = commandParts.component2().lowercase()
    val from = Position(commandParts.component3())
    val to = Position(commandParts.component4())
    return Command(
        when(onNOff){
            "" -> CommandType.TOGGLE
            "on" -> CommandType.ON
            "off" -> CommandType.OFF
            else -> error("Invalid Command.")
        }
        , from, to
    )
}

