package day4

import java.math.BigInteger
import java.security.MessageDigest

fun main(){
    val input = "iwrupvqb"
    val part1Solution = getSolution(5, input)
    val part2Solution = getSolution(6, input)

    println("Part1 $part1Solution")
    println("Part2 $part2Solution")

}

fun String.toMD5(): String {
    val md = MessageDigest.getInstance("MD5")
    val bigInt = BigInteger(1, md.digest(this.toByteArray(Charsets.UTF_8)))
    return String.format("%032x", bigInt)
}

fun getSolution(nZeros: Int, input: String): Int{
    var count = 0
    do{
        count++
    }
    while((input + count).toMD5().take(nZeros).any { it != '0' })
    return count
}