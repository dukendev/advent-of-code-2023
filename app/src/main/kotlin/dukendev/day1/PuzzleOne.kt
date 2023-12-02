package dukendev.day1

import java.io.File

class PuzzleOne {


    private val spelledDigits =
        setOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    private val reveredDigits = spelledDigits.map {
        it.reversed()
    }
    private val startMatch = setOf('o', 't', 'f', 'e', 'n', 's')
    private val endMatch = setOf('e', 'o', 'r', 'x', 'n', 't')

    private fun getInputData(): List<String> {
        val file =
            File("/Users/sanjeetyadav/dev/advent-of-code-2023/app/src/main/kotlin/dukendev/day1/input.txt")
        return file.readLines()
    }

    /**
     * @input : each line has alpha numerals string
     * @calibration : each line first and last num as first and second digit of num
     * @answer : sum of all calibration values
     **/

    fun getSumOfCalibrationValues(input: List<String> = getInputData()): Int {
        return input.sumOf {
            val digits = parseSpelledNumerals(it)
            val x = getCalibrationValue(digits.first, digits.second)
            println(x)
            x
        }
    }

    fun parseSpelledNumerals(s: String): Pair<Int, Int> {
        println("---")
        println("matching : $s")
        val numeralDigits: Pair<Pair<Int, Int>, Pair<Int, Int>> = getNumeralDigits(s)
        println("numeral : ${numeralDigits.first} and ${numeralDigits.second}")
        val spelledDigits: Pair<Pair<Int, Int>, Pair<Int, Int>> = getSpelledDigits(s)
        println("spell : ${spelledDigits.first} and ${spelledDigits.second}")

        val finalList : List<Pair<Int,Int>> = listOf(numeralDigits.first,numeralDigits.second,spelledDigits.first,spelledDigits.second).filter {
            it.first in 0..9999
        }
        var first = 0
        var second = 0
        var max = Int.MIN_VALUE
        var min = Int.MAX_VALUE
        for(p in finalList){
            max = maxOf(p.first,max)
            min = minOf(p.first,min)
        }

        for(p in finalList) {
            if(p.first == min){
                first = p.second
            }
            if(p.first == max){
                second = p.second
            }
        }

        println("final : ${first} and ${second}")
        return Pair(first, second)
    }

    private fun getSpelledDigits(s: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        var first: Pair<Int, Int> = Pair(Int.MAX_VALUE, 0)
        var last: Pair<Int, Int> = Pair(Int.MIN_VALUE, 0)
        var i = 0
        var j = 2
        while (i < s.length && j < s.length) {
            if (startMatch.contains(s[i])) {
                val guess = s.slice(i..j)
                if (spelledDigits.contains(guess)) {
                    first = Pair(i, guess.mapSpell())
                    break
                } else {
                    j++
                }
            } else {
                i++
            }
            //reset
            if (j - i > 6) {
                i++
                j = i + 2
            }
        }
        i = 0
        j = 2
        val r = s.reversed()
        while (i < r.length && j < r.length) {
            if (endMatch.contains(r[i])) {
                val guess = r.slice(i..j)
                if (reveredDigits.contains(guess)) {
                    last = Pair(r.length - j - 1, guess.reversed().mapSpell())
                    break
                } else {
                    j++
                }
            } else {
                i++
                j++
            }
            //reset
            if (j - i > 6) {
                i++
                j = i + 2
            }
        }

        return Pair(first, last)
    }

    private fun getNumeralDigits(s: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {

        if (s.all { !it.isDigit() }) {
            return Pair(Pair(Int.MAX_VALUE, 0), Pair(Int.MIN_VALUE, 0))
        }

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
                    return Pair(Pair(i, s[i].digitToInt()), Pair(j, s[j].digitToInt()))
                }

                else -> {
                    i++
                    j--
                }
            }
        }
        return Pair(Pair(i, s[i].digitToInt()), Pair(j, s[j].digitToInt()))
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

    private fun String.mapSpell(): Int {
        return when (this) {
            "one" -> 1
            "two" -> 2
            "three" -> 3
            "four" -> 4
            "five" -> 5
            "six" -> 6
            "seven" -> 7
            "eight" -> 8
            "nine" -> 9
            else -> 0
        }
    }

    private fun getCalibrationValue(first: Int, second: Int): Int = 10.times(first).plus(second)


}