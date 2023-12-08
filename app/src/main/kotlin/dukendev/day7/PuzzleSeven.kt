package dukendev.day7

import dukendev.util.Util

class PuzzleSeven {

    fun calcWinnings() {
        val input = Util.getInputData(test)
        val cards = input.map {
            CamelCard(
                bid = it.split(" ")[1].trim().toInt(),
                hand = it.split(" ")[0].trim().toCharArray().toList(),
                typeOrder = 0
            )
        }.map {
            it.copy(typeOrder = it.hand.findOrder())
        }.map { card ->
            card.copy(hand = card.hand.sortedBy { it.getCardValue() })
        }
        val orderedLists =
            cards.sortedBy { it.typeOrder }.groupBy { it.typeOrder }.map { it.value }.map { list -> list.sortByHighCards() }
        val sortedCards = orderedLists.flatten()
       println(sortedCards)
        var sum = 0
        sortedCards.forEachIndexed { index, camelCard ->
            sum += (index+1).times(camelCard.bid)
        }
        println(sum)
    }

    private fun List<CamelCard>.sortByHighCards(): List<CamelCard> {
        return this
            .asSequence()
            .sortedBy { it.hand[0].getCardValue() }
            .sortedBy { it.hand[1].getCardValue() }
            .sortedBy { it.hand[2].getCardValue() }
            .sortedBy { it.hand[3].getCardValue() }
            .sortedBy { it.hand[4].getCardValue() }
            .toList()
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

    private fun List<Char>.findOrder(): Int {
        val frequency = mutableMapOf<Char, Int>()
        for (c in this) {
            if (frequency.contains(c)) {
                frequency[c] = frequency[c]!! + 1
            } else {
                frequency.putIfAbsent(c, 1)
            }
        }
        //find order from frequency map
        return when (frequency.keys.size) {
            1 -> 7
            2 -> if (frequency.keys.any { frequency[it] == 4 }) 6 else 5
            3 -> if (frequency.keys.any { frequency[it] == 3 }) 4 else 3
            4 -> 2
            else -> 1
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