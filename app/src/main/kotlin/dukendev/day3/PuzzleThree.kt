package dukendev.day3

import dukendev.util.Util.Companion.getInputData

class PuzzleThree {

    private val path =
        "/Users/sanjeetyadav/dev/adventOfCode2023/advent-of-code-2023-kotlin/app/src/main/kotlin/dukendev/day3/input.txt"

    private val testPath =
        "/Users/sanjeetyadav/dev/adventOfCode2023/advent-of-code-2023-kotlin/app/src/main/kotlin/dukendev/day3/test.txt"

    fun findSumOfEngineParts() {
        val lines = getInputData(testPath)
        val grid = lines.map {
            it.toCharArray().toList()
        }
        val valid = mutableListOf<Int>()
        val cols = grid[0].size - 1
        val rows = grid.size - 1
        val num = mutableListOf<Char>()
        for (i in 0..rows) {
            var matchStart = cols
            for (j in 0..cols) {
                val c = grid[i][j]
                if (c.isDigit()) {
                    num.add(grid[i][j])
                    matchStart = minOf(matchStart, j)
                    println("number building : $num and start match $matchStart")
                } else {
                    //check to push in main list or clear
                    if (num.isNotEmpty()) {
                        println("validating $num from $matchStart to ${j - 1}")
                        var isValid = true
                        for (digit in matchStart until j) {
                            isValid = isValid && alone(
                                r = i,
                                c = digit,
                                grid = grid,
                                isFirst = digit == matchStart,
                                isLast = digit == j - 1
                            )

                        }
                        if (!isValid) {
                            println("found ${num.toNumber()} at ($i,${j - 1})")
                            valid.add(num.toNumber())
                        }
                        num.clear()
                        matchStart = cols
                    }

                }
            }
        }
        println(valid)
        println(valid.sum())
    }

    private fun List<Char>.toNumber(): Int {
        return this.map { it.digitToInt() }.fold(0) { acc, digit ->
            acc * 10 + digit
        }
    }


    private fun alone(
        r: Int, c: Int, grid: List<List<Char>>, isLast: Boolean = false, isFirst: Boolean = false
    ): Boolean {
        print("checking ${grid[r][c]} at ($r,$c)")
        val up = (if (r > 0) grid.getOrNull(r - 1)?.get(c)  else '.') == '.'
        val down = (if (r < grid.size - 1) grid.getOrNull(r + 1)?.get(c) else '.') == '.'
        val back = grid[r].getOrElse(c - 1) { '.' } == '.'
        val diaBackDown =
            (if (r < grid.size - 1) grid.getOrNull(r + 1)?.getOrElse(c - 1) { '.' } else '.') == '.'
        val diaBackUp =
            (if (r > 0) grid.getOrNull(r - 1)?.getOrElse(c - 1) { '.' } else '.') == '.'
        val diaFrontUp =
            (if (r > 0) grid.getOrNull(r - 1)?.getOrElse(c + 1) { '.' } else '.') == '.'
        val diaFrontDown =
            (if (r < grid.size - 1) grid.getOrNull(r + 1)?.getOrElse(c + 1) { '.' } else '.') == '.'
        val front = grid[r].getOrElse(c + 1) { '.' } == '.'

        val representation = listOf(
            listOf(diaBackUp.printC(), up.printC(), diaFrontUp.printC()),
            listOf(back.printC(), 'd', front.printC()),
            listOf(diaBackDown.printC(), down.printC(), diaFrontDown.printC())
        )

        println()
        for (rw in representation) {
            for (cl in rw) {
                print(" $cl ")
            }
            println()
        }

        return when {
            isFirst && !isLast -> {
                val first = down && up && back && diaBackUp && diaBackDown && diaFrontDown && diaFrontUp
                println("first alone $first")
                first
            }

            isLast && !isFirst -> {
                val last = down && up && front && diaFrontUp && diaFrontDown && diaBackUp && diaBackDown
                println("last alone : $last")
                last
            }

            else -> {
                val mid = down && up && diaBackUp && diaBackDown && diaFrontDown && diaFrontUp
                println("mid alone : $mid")
                mid
            }
        }
    }

    private fun Boolean.printC(): Char {
        return if (this) '.' else '+'
    }

}