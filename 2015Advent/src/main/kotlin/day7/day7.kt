package day7

import java.io.File

data class Wire(val id: String, val value: Int)

sealed class Operation(val action: () -> Int)

class And(leftOperand: Int, rightOperand: Int): Operation({
    leftOperand and rightOperand
})

class Or(leftOperand: Int, rightOperand: Int): Operation({
    leftOperand or rightOperand
})

class RShift(leftOperand: Int, times: Int): Operation({
    leftOperand shr times
})

class LShift(leftOperand: Int, times: Int): Operation({
    leftOperand shl times
})

class Not(operand: Int): Operation({
        operand.inv()
})

class Assign(value: Int, id: String, map: MutableMap<String, Wire>): Operation({
    map[id] = Wire(id, value)
    value
})

fun main(){
    val operations = File("src\\main\\kotlin\\day6\\day6.txt")
        .readLines()
    val wireMap = mutableMapOf<String, Wire>()
}