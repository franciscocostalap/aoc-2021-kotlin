import java.io.File
import java.util.ArrayList

private class AOCTestException(message: String): Exception(message)

fun <T>File.mapLines(transform: (String) -> T ): List<T> {
    val result = ArrayList<T>()
    forEachLine(Charsets.UTF_8) { result.add(transform(it)) }
    return result
}

fun getFile(day:Int) = File("src/main/kotlin/day$day/day$day.txt")

fun getTest(day: Int) = File("src/main/kotlin/day$day/day${day}Test.txt")

fun <T>part1(value: T) = println("Part1: $value")

fun <T> part2(value: T) = println("Part2: $value")

fun <T>checkTest(expected: T, actual: T, title: String){
    if(expected == actual) println("Test $title correct. expected: $expected  actual: $actual")
    else throw AOCTestException(
        """Test $title failed expected: "$expected" but was "$actual" """
    )
}

/**
 * Returns a list containing the results of applying the
 * given [transform] function to all of the combinations
 * of elements from this iterable and [other] iterable.
 *
 * Only elements from distinct iterables are combined.
 */
fun <T, U, V> Iterable<T>.combinations(other: Iterable<U>, transform:(T, U) -> V):List<V>
        = flatMap { thisIt ->
    other.map { otherIt -> transform(thisIt, otherIt) }
}

val lineSeparator = System.lineSeparator()





