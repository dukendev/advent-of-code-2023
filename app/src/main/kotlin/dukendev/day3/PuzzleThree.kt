package dukendev.day3

import dukendev.util.Util.Companion.getInputData

class PuzzleThree {

    fun findSumOfEngineParts1() {
        val lines = getInputData(path)
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
                    num.add(c)
                    matchStart = minOf(matchStart, j)
                    if (j == cols) {
                        var isValid: Boolean
                        for (digit in matchStart until j) {
                            isValid = checkNeighbours(
                                r = i, c = digit, grid = grid
                            )
                            if (isValid) {
                                valid.add(num.toNumber())
                                break
                            }
                        }
                        matchStart = cols
                        num.clear()
                    }
                } else {
                    if (num.isNotEmpty()) {
                        var isValid: Boolean
                        for (digit in matchStart until j) {
                            isValid = checkNeighbours(
                                r = i, c = digit, grid = grid
                            )
                            if (isValid) {
                                valid.add(num.toNumber())
                                break
                            }
                        }
                    }
                    matchStart = cols
                    num.clear()
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

    private fun checkNeighbours(
        r: Int, c: Int, grid: List<List<Char>>
    ): Boolean {
        var hasPart = false
        outer@ for (rr in listOf(-1, 0, 1)) {
            for (cc in listOf(-1, 0, 1)) {
                val curR = r - rr
                val curC = c - cc
                if (curR >= grid.size || curR <= 0 || curC >= grid[0].size || curC <= 0) {
                    continue
                }
                if (grid[curR][curC] != '.' && !grid[curR][curC].isDigit()) {
                    hasPart = true
                }
                if (hasPart) {
                    break@outer
                }
            }
        }
        return hasPart
    }

    companion object {
        const val path =
            "/Users/sanjeetyadav/dev/adventOfCode2023/advent-of-code-2023-kotlin/app/src/main/kotlin/dukendev/day3/input.txt"
        const val testPath =
            "/Users/sanjeetyadav/dev/adventOfCode2023/advent-of-code-2023-kotlin/app/src/main/kotlin/dukendev/day3/test.txt"
    }

}