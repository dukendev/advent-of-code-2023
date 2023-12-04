package dukendev.util

import java.io.File

class Util {
    companion object {

        fun getInputData(path : String): List<String> {
            val file =
                File(path)
            return file.readLines()
        }
    }
}