import java.io.File
import java.nio.charset.Charset
import java.util.ArrayList


fun <T>File.mapLines(transform:(String) -> T): List<T> {
    val result = ArrayList<T>()
    forEachLine(Charsets.UTF_8) { result.add(transform(it)); }
    return result
}