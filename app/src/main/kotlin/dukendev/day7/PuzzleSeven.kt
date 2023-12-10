package dukendev.day7

import dukendev.util.Util

class PuzzleSeven {

    fun calcWinnings() {
        val input = Util.getInputData(input)
        val sum = input.asSequence().map {
            CamelCard(
                bid = it.split(" ")[1].trim().toInt(),
                hand = it.split(" ")[0].trim().toCharArray().toList(),
                typeOrder = 0,
            )
        }.map {
            it.copy(typeOrder = it.hand.findOrder())
        }.sortedBy { it.typeOrder }
            .groupBy { it.typeOrder }
            .map { it.value }
            .map { list -> list.sortByHighCards() }
            .flatten()
            .foldIndexed(0) { index, acc, camelCard ->
                acc + (index + 1) * camelCard.bid
            }
        println(sum)
    }

    private fun List<CamelCard>.sortByHighCards(): List<CamelCard> {
        val comparator = Comparator<CamelCard> { card1, card2 ->
            for (i in 0 until card1.hand.size) {
                val diff = card1.hand[i].getCardValue() - card2.hand[i].getCardValue()
                if (diff != 0) {
                    return@Comparator diff
                }
            }
            return@Comparator 0
        }
        return this.sortedWith(comparator)
    }

    private fun Char.getCardValue(): Int {
        return when (this) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> 11
            'T' -> 10
            else -> this.digitToInt()
        }
    }

    private fun List<Char>.calculateFrequency(): MutableMap<Char, Int> {
        val frequency = mutableMapOf<Char, Int>()
        for (c in this) {
            if (frequency.contains(c)) {
                frequency[c] = frequency[c]!! + 1
            } else {
                frequency.putIfAbsent(c, 1)
            }
        }
        return frequency
    }

    private fun List<Char>.findOrder(): Int {
        val frequency = this.calculateFrequency()
        //find order from frequency map
        return when (frequency.keys.size) {
            1 -> 7 //five of a kind
            2 -> if (frequency.keys.any { frequency[it] == 4 }) 6 else 5 //four of a kind or full house
            3 -> if (frequency.keys.any { frequency[it] == 3 }) 4 else 3 //three of a kind or 2 pair
            4 -> 2 // 1 pair
            else -> 1 // high card
        }
    }

    companion object {
        const val input =
            "/Users/sanjeetyadav/dev/adventOfCode2023/advent-of-code-2023-kotlin/app/src/main/kotlin/dukendev/day7/input.txt"
        const val test =
            "/Users/sanjeetyadav/dev/adventOfCode2023/advent-of-code-2023-kotlin/app/src/main/kotlin/dukendev/day7/test.txt"
    }
}

data class CamelCard(
    val hand: List<Char>, val bid: Int, val typeOrder: Int
)