package dukendev.util

import java.io.File

class Util {
    companion object {

        fun getInputData(path: String): List<String> {
            val file = File(path)
            return file.readLines()
        }

        fun getRawInputData(path: String): String {
            val file = File(path)
            return file.readText()
        }

        fun String.getNumberList(): List<Long> {
            return this.split(" ").filter { it != "" }.map { it.trim().toLong() }
        }
    }
}