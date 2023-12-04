package dukendev.day3

import dukendev.util.Util.Companion.getInputData

class PuzzleThree {

    private val path =
        "/Users/sanjeetyadav/dev/adventOfCode2023/advent-of-code-2023-kotlin/app/src/main/kotlin/dukendev/day3/input.txt"

    private val testPath = "/Users/sanjeetyadav/dev/adventOfCode2023/advent-of-code-2023-kotlin/app/src/main/kotlin/dukendev/day3/test.txt"

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
            for (j in 0..cols) {
                val c = grid[i][j]
                if (c.isDigit()) {
                    if (notValidNumber(
                            r = i,
                            c = j,
                            grid = grid,
                            isFirst = num.isEmpty(),
                            isLast = !(grid[i].getOrElse(j + 1) { '.' }.isDigit())
                        )
                    ) {
                        println("not valid number ${num.toNumber()} at ($i,$j)")
                        num.clear()
                    } else {
                        num.add(grid[i][j])
                    }
                } else {
                    //check to push in main list or clear
                    if (num.isNotEmpty()) {
                        println("found ${num.toNumber()} at ($i,$j)")
                        valid.add(num.toNumber())
                        num.clear()
                    }
                }
            }
        }
        println(valid.sum())
    }

    private fun List<Char>.toNumber(): Int {
        return this.map { it.digitToInt() }.fold(0) { acc, digit ->
            acc * 10 + digit
        }
    }


    private fun notValidNumber(
        r: Int, c: Int, grid: List<List<Char>>, isLast: Boolean = false, isFirst: Boolean = false
    ): Boolean {
        return when {
            isFirst && !isLast -> {
                grid.getOrNull(r - 1)?.get(c) == '.' && grid.getOrNull(r + 1)
                    ?.get(c) == '.' && grid[r].getOrElse(c - 1) { '.' } == '.' && grid.getOrNull(r + 1)
                    ?.getOrElse(c - 1) { '.' } == '.' && grid.getOrNull(r - 1)
                    ?.getOrElse(c - 1) { '.' } == '.'
            }

            isLast && !isFirst -> {
                grid.getOrNull(r - 1)?.get(c) == '.' && grid.getOrNull(r + 1)
                    ?.get(c) == '.' && grid[r].getOrElse(c + 1) { '.' } == '.' && grid.getOrNull(r + 1)
                    ?.getOrElse(c + 1) { '.' } == '.' && grid.getOrNull(r - 1)
                    ?.getOrElse(c + 1) { '.' } == '.'
            }

            else -> {
                grid.getOrNull(r - 1)?.get(c) == '.' && grid.getOrNull(r + 1)?.get(c) == '.'
            }
        }
    }

}