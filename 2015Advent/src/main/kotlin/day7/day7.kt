package day7

import getFile
import mapLines
import org.jsoup.HttpStatusException

val wireMap = mutableMapOf<Id, Int?>()
typealias Id = String

sealed class BitwiseOperation(val resultId: Id, private val action: () -> Boolean) {
    companion object {
        private val regex = """([a-z]+|\d+)? ?(AND|OR|LSHIFT|RSHIFT|NOT)? ?([a-z]+|\d+) -> ([a-z]+)""".toRegex()

        fun parse(string: String): BitwiseOperation {
            val content = regex.find(string)?.destructured?.toList() ?: error("Invalid operation.")
            val (left, operation, right, result) = content
            return when(operation.uppercase()){
                "AND" -> And(left, right, result)
                "OR" -> Or(left, right, result)
                "LSHIFT" -> LShift(left, right, result)
                "RSHIFT" -> RShift(left, right, result)
                "NOT" -> Not(left+right, result)
                "" -> Assign(left+right, result)
                else -> error("Invalid Operation.")
            }
        }
    }

    fun execute(): Boolean = if (wireMap[resultId] != null) false else action()
}

sealed class BinaryOperation
    (
    leftOperand: String,
    rightOperand: String,
    resultID: Id,
    builtInOperation: Int.(Int) -> Int
):
    BitwiseOperation(resultID, lambda@{
        val leftValue = leftOperand.toValue()
        val rightValue = rightOperand.toValue()
        val result = rightValue?.let { checkedRight -> leftValue?.builtInOperation(checkedRight) }
            ?: return@lambda false
        wireMap[resultID] = result
        true
    })

val test = listOf(
    "123 -> x",
    "456 -> y",
    "x AND y -> d",
    "x OR y -> e",
    "x LSHIFT 2 -> f",
    "y RSHIFT 2 -> g",
    "NOT x -> h",
    "NOT y -> i",
)

fun main() {
    val operations = getFile(7).mapLines(BitwiseOperation::parse)
    //val operations = test.map(BitwiseOperation::parse)
    val part1Solution = operations.inferSignal("a") ?: error("No Solution!")
    // Part2
    wireMap.resetValues()
    wireMap["b"] = part1Solution
    val part2Solution = operations.inferSignal("a")
    println("Part1: $part1Solution")
    println("Part2: $part2Solution")
}

fun List<BitwiseOperation>.inferSignal(wireId: Id): Int? =
    if(wireMap[wireId] != null)
        wireMap[wireId]
    else
        filterNot(BitwiseOperation::execute).inferSignal(wireId)


fun String.toValue(): Int? = if(this.all{it.isDigit()}) toInt() else wireMap[this]

data class And(val leftOperand: String,  val rightOperand: String, val resultID: Id) :
    BinaryOperation(leftOperand, rightOperand, resultID, Int::and)

data class Or(val leftOperand: String,  val rightOperand: String, val resultID: Id) :
    BinaryOperation(leftOperand, rightOperand, resultID, Int::or)

data class RShift(val leftOperand: String,  val rightOperand: String, val resultID: Id) :
    BinaryOperation(leftOperand, rightOperand, resultID, Int::ushr)

data class LShift(val leftOperand: String,  val rightOperand: String, val resultID: Id) :
    BinaryOperation(leftOperand, rightOperand, resultID, Int::shl)


data class Not(val rightOperand: String, val resultID: Id) : BitwiseOperation(resultID, action@{
    val rightValue = rightOperand.toValue()
    val result = rightValue?.toUShort()?.inv()?.toInt() ?: return@action false
    wireMap[resultID] = result
    true
})

data class Assign(val rightOperand: String, val resultID: Id) : BitwiseOperation(resultID, action@{
    val rightValue = rightOperand.toValue() ?: return@action false
    wireMap[resultID] = rightValue
    true
})

fun MutableMap<Id, Int?>.resetValues() =
    this.forEach {
        wireMap[it.key] = null
    }
