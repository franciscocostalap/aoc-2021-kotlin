import kotlinx.coroutines.*
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.text.SimpleDateFormat

/**
 * Advent of code template builder for Intellij project
 *
 * This template Builds a whole folder for the day containing:
 *  - kotlin file located in dayN/dayN.kt (where N is the number of the challenge)
 *  - input file located in dayN/dayN.txt -> works for all days of all years
 *  - test input located in dayN/dayNTest.txt -> Not 100% for all years, sometimes hidden in text
 *  - challenge article file located in dayN/dayNArticle.txt -> works for all days of all years
 *
 *
 * How to use:
 *
 * Change to the day you want on AOCTemplateBuilder instantiation in the "build.kt" main function.
 *
 * Change to the year you want on AOCTemplateBuilder instantiation "build.kt" main function.
 *
 * Create an environment variable named TOKEN with your advent of code session ID.
 *
 * Run build.kt script.
 *
 *
 * How to get your AOC session ID:
 *
 * 1º login to advent of code in chrome.
 *
 * 2º Open dev tools.
 *
 * 3º Navigate to the network tab
 *
 * 4º Press Ctrl+R to record requests
 *
 * 5º open any requested resource
 *
 * 6º Your session Id is in the header "cookie", copy the whole line beginning at "session=".
 *
 *
 *
 * Gradle dependencies (Versions of when the template was built):
 *  "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-RC"
 *  "org.jsoup:jsoup:1.14.3"
 *
 * @author Francisco Costa 2021
 */
class AOCChallengeTemplateCreator(private val day: Int, private val year:Int){

    companion object {
        private const val COULD_NOT_REACH = "Could not reach AOC."
        private const val CHALLENGE_UNAVAILABLE = "Challenge not available yet."
    }
    private val aocBaseUri = "https://adventofcode.com/${this.year}/day/$day"
    private val challengeAvailable = checkChallengeTime(day)

    init {
        try {
            buildTemplate()
        } catch (error: IllegalStateException) {
            println("Error: ${error.message}")
        }catch (httpError: HttpStatusException){
            println("Could not reach AOC.")
        }
    }

    private suspend fun fetchChallengeInput(): String =
        fetch("$aocBaseUri/input") ?: error(COULD_NOT_REACH)


    private fun getTestInput(document: Document): String = document.select("code")[0].text()

    private fun getPuzzleArticle(document: Document): String {
        val titleAndArticle = document.select("article")[0].wholeText()
        val title = titleAndArticle.substringAfter("---").substringBefore("---")
        val article = System.lineSeparator() + titleAndArticle.substringAfter("---")
            .substringAfter("---")
        return title + article
    }

    private fun getChallengeDocument() =
        runBlocking { Jsoup.connect(aocBaseUri).header("cookie", System.getenv("TOKEN")).get() }

    private fun buildTemplate() {
        val folder = "day$day"
        createFolder(folder)
        runBlocking {
            writeToAOCFile(readFromTemplate().replace("%d", "$day"), "$folder/$folder.kt")
            check(challengeAvailable){ CHALLENGE_UNAVAILABLE }
            val document = getChallengeDocument()
            writeToAOCFile(fetchChallengeInput(), "$folder/$folder.txt")
            writeToAOCFile(getTestInput(document), "$folder/${folder}Test.txt")
            writeToAOCFile(getPuzzleArticle(document), "$folder/${folder}Article.txt")
        }
    }

    private fun checkChallengeTime(day: Int): Boolean {
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val timeZoneOffset = format.timeZone.rawOffset
        val date = "${this.year}/12/$day ${(5 + timeZoneOffset) % 24}:00:00"// UTC Timezone
        val challengeTimeMilis = format.parse(date).time
        val currentMilis = System.currentTimeMillis()
        return currentMilis > challengeTimeMilis
    }

}

private const val CODE_PATH = "src/main/kotlin"

private fun createFolder(name: String) {
    val folder = File(CODE_PATH, name)
    if (folder.exists()) println("Folder $name already exists in $CODE_PATH.")
    folder.mkdir()
}

private fun writeToAOCFile(text: String, name: String) {
    val file = File(CODE_PATH, name)
    if(file.exists() && name.substringAfter(".") == "kt")
        println("If you want to overwrite $name delete it and run the script again.")
    else file.writeText(text)
}

private fun readFromTemplate() = File(CODE_PATH, "aoc-template").readText()

private suspend fun fetch(uri: String): String? {
    val client = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder()
        .uri(URI.create(uri)).setHeader("cookie", System.getenv("TOKEN"))
        .build()
    return runBlocking {
        val response = withContext(Dispatchers.IO) { client.send(request, HttpResponse.BodyHandlers.ofString()) }
        response.body()
    }
}
