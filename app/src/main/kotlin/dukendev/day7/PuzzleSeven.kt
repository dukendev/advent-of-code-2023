package dukendev.day7

import dukendev.util.Util

class PuzzleSeven {

    fun calcWinnings() {
        val input = Util.getInputData(input)
        val cards = input.map {
            CamelCard(
                bid = it.split(" ")[1].trim().toInt(),
                hand = it.split(" ")[0].trim().toCharArray().toList(),
                typeOrder = 0,
                freqMap = mapOf()
            )
        }.map {
            it.copy(typeOrder = it.hand.findOrder())
        }.map { card ->
            card.copy(hand = card.hand.sortedBy { it.getCardValue() })
        }.map {
            it.copy(freqMap = it.hand.calculateFrequency())
        }

        val orderedLists = cards.sortedBy { it.typeOrder }.groupBy { it.typeOrder }.map { it.value }
            .map { list -> list.sortByHighCards() }
        val sortedCards = orderedLists.flatten()
        println(sortedCards)
        var sum = 0
        sortedCards.forEachIndexed { index, camelCard ->
            sum += (index + 1).times(camelCard.bid)
        }
        println(sum)
    }

    private fun List<CamelCard>.sortByHighCards(): List<CamelCard> {
        val sortedList = mutableListOf<CamelCard>()
        when (this.first().typeOrder) {
            7 -> {
                sortedList.addAll(this.sortedBy { it.hand.first().getCardValue() })
            }

            6, 5 -> {
                sortedList.addAll(this.sortedBy {
                    if (it.freqMap[it.hand.first()]!! < it.freqMap[it.hand.last()]!!) {
                        it.hand.first().getCardValue()
                    } else {
                        it.hand.last().getCardValue()
                    }
                }.sortedBy {
                    if (it.freqMap[it.hand.first()]!! > it.freqMap[it.hand.last()]!!) {
                        it.hand.first().getCardValue()
                    } else {
                        it.hand.last().getCardValue()
                    }
                })
            }
            // todo : impl for 4 to 2 case for type order
            else -> {
                sortedList.addAll(
                    this.asSequence().sortedBy { it.hand[0].getCardValue() }
                        .sortedBy { it.hand[1].getCardValue() }
                        .sortedBy { it.hand[2].getCardValue() }
                        .sortedBy { it.hand[3].getCardValue() }
                        .sortedBy { it.hand[4].getCardValue() }.toList()
                )
            }
        }
        return sortedList
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
    val hand: List<Char>, val bid: Int, val typeOrder: Int, val freqMap: Map<Char, Int>
)