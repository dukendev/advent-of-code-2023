package dukendev.day1

import java.io.File

class PuzzleOne {

    private fun getInputData(): List<String> {
        val file =
            File("/Users/sanjeetyadav/dev/advent-of-code-2023/app/src/main/kotlin/dukendev/day1/part1.txt")
        return file.readLines()
    }

    /**
     * @input : each line has alpha numerals string
     * @calibration : each line first and last num as first and second digit of num
     * @answer : sum of all calibration values
     **/

    fun getSumOfCalibrationValues(input : List<String> = getInputData()): Int {
        return input.sumOf {
            val digits = parseNumerals(it)
            getCalibrationValue(digits.first, digits.second)
        }
    }

    fun parseNumerals(s: String): Pair<Int, Int> {
        var i = 0
        var j = s.length - 1
        while (i < j) {
            when {
                s[i].isDigit() && !s[j].isDigit() -> {
                    j--
                }
                s[j].isDigit() && !s[i].isDigit() -> {
                    i++
                }
                s[i].isDigit() && s[j].isDigit() -> {
                    return Pair(s[i].digitToInt(), s[j].digitToInt())
                }
                else -> {
                    i++
                    j--
                }
            }
        }
        return Pair(s[i].digitToInt(), s[j].digitToInt())
    }
    private fun getCalibrationValue(first: Int, second: Int): Int = 10.times(first).plus(second)


}