package dukendev.day5

import dukendev.day5.Almanac.Companion.getAlmanac
import dukendev.day5.Almanac.Companion.getAlmanacMap
import dukendev.day5.AlmanacRevised.Companion.getAlmanacRevised
import dukendev.util.Util
import dukendev.util.Util.Companion.getNumberList

class PuzzleFive {

    fun findNearestLocations(isPart2: Boolean = true) {
        if (!isPart2) {
            val almanac = getAlmanacFromInput()
            val nearest = almanac.seeds.asSequence().map {
                almanac.seedToSoilMap.getMapping(it)
            }.map {
                almanac.soilToFertilizerMap.getMapping(it)
            }.map {
                almanac.fertilizerToWaterMap.getMapping(it)
            }.map {
                almanac.waterToLightMap.getMapping(it)
            }.map {
                almanac.lightToTemperatureMap.getMapping(it)
            }.map {
                almanac.temperatureToHumidity.getMapping(it)
            }.map {
                almanac.humidityToLocationMap.getMapping(it)
            }.min()
            println(nearest)
        } else {
            val almanac = getAdvanceAlmanacFromInput()
            val minLocations = mutableListOf<Long>()
            almanac.seeds.sortedBy { it.first }.forEach { seedRange ->
                    println("checking for $seedRange")
                    val nearestInRange = seedRange.asSequence().map {
                        almanac.seedToSoilMap.getMapping(it)
                    }.map {
                        almanac.soilToFertilizerMap.getMapping(it)
                    }.map {
                        almanac.fertilizerToWaterMap.getMapping(it)
                    }.map {
                        almanac.waterToLightMap.getMapping(it)
                    }.map {
                        almanac.lightToTemperatureMap.getMapping(it)
                    }.map {
                        almanac.temperatureToHumidity.getMapping(it)
                    }.map {
                        almanac.humidityToLocationMap.getMapping(it)
                    }.min()
                    minLocations.add(nearestInRange)
                    println("found $nearestInRange")
                }
            println(minLocations.min())
        }
    }


    private fun getAdvanceAlmanacFromInput(): AlmanacRevised {
        val input = Util.getRawInputData(path)
        val infoParts = input.split("\n\n")
        return infoParts.getAlmanacRevised()
    }

    private fun getAlmanacFromInput(): Almanac {
        val input = Util.getRawInputData(path)
        val infoParts = input.split("\n\n")
        return infoParts.getAlmanac()
    }


    private fun List<Pair<LongRange, LongRange>>.getAdvanceMapping(source: Long): Long {
        var mappedValue: Long? = null
        for (r in this) {
            if (source in r.first) {
                mappedValue = findMappingFromRanges(r.first, r.second, source)
                break
            }
        }
        println("found mapped $source -> $mappedValue")
        return mappedValue ?: source
    }

    private fun List<Pair<LongRange, LongRange>>.getMapping(source: Long): Long {
        var mappedValue: Long? = null
        for (r in this) {
            if (source in r.first) {
                mappedValue = findMappingFromRanges(r.first, r.second, source)
                break
            }
        }
        println("found mapped $source -> $mappedValue")
        return mappedValue ?: source
    }

    private fun findMappingFromRanges(
        source: LongRange, destination: LongRange, target: Long
    ): Long {
        val low = source.first
        val high = source.last
        val mid = low + (high - low) / 2
        return if (target >= mid) {
            val offset = source.last - target
            destination.last - offset
        } else {
            val offset = target - source.first
            destination.first + offset
        }
    }

    companion object {
        const val testPath =
            "/Users/sanjeetyadav/dev/adventOfCode2023/advent-of-code-2023-kotlin/app/src/main/kotlin/dukendev/day5/test.txt"

        const val path =
            "/Users/sanjeetyadav/dev/adventOfCode2023/advent-of-code-2023-kotlin/app/src/main/kotlin/dukendev/day5/input.txt"
    }

}


data class AlmanacRevised(
    val seeds: List<LongRange>,
    val seedToSoilMap: List<Pair<LongRange, LongRange>>,
    val soilToFertilizerMap: List<Pair<LongRange, LongRange>>,
    val fertilizerToWaterMap: List<Pair<LongRange, LongRange>>,
    val waterToLightMap: List<Pair<LongRange, LongRange>>,
    val lightToTemperatureMap: List<Pair<LongRange, LongRange>>,
    val temperatureToHumidity: List<Pair<LongRange, LongRange>>,
    val humidityToLocationMap: List<Pair<LongRange, LongRange>>
) {
    companion object {
        fun List<String>.getAlmanacRevised(): AlmanacRevised {
            return AlmanacRevised(seeds = this[0].split(":")[1].getNumberList().getSeedRange(),
                seedToSoilMap = this[1].split("\n").getAlmanacMap().sortedBy { it.first.first },
                soilToFertilizerMap = this[2].split("\n").getAlmanacMap()
                    .sortedBy { it.first.first },
                fertilizerToWaterMap = this[3].split("\n").getAlmanacMap()
                    .sortedBy { it.first.first },
                waterToLightMap = this[4].split("\n").getAlmanacMap().sortedBy { it.first.first },
                lightToTemperatureMap = this[5].split("\n").getAlmanacMap()
                    .sortedBy { it.first.first },
                temperatureToHumidity = this[6].split("\n").getAlmanacMap()
                    .sortedBy { it.first.first },
                humidityToLocationMap = this[7].split("\n").getAlmanacMap()
                    .sortedBy { it.first.first })
        }

        private fun List<Long>.getSeedRange(): List<LongRange> {
            val list = mutableListOf<LongRange>()
            for ((i, _) in this.withIndex()) {
                if (i.mod(2) != 0) {
                    list.add(this[i - 1]..this[i - 1] + this[i])
                }
            }
            return list
        }
    }
}

data class Almanac(
    val seeds: List<Long>,
    val seedToSoilMap: List<Pair<LongRange, LongRange>>,
    val soilToFertilizerMap: List<Pair<LongRange, LongRange>>,
    val fertilizerToWaterMap: List<Pair<LongRange, LongRange>>,
    val waterToLightMap: List<Pair<LongRange, LongRange>>,
    val lightToTemperatureMap: List<Pair<LongRange, LongRange>>,
    val temperatureToHumidity: List<Pair<LongRange, LongRange>>,
    val humidityToLocationMap: List<Pair<LongRange, LongRange>>
) {
    companion object {
        fun List<String>.getAlmanac(): Almanac {
            return Almanac(
                seeds = this[0].split(":")[1].getNumberList(),
                seedToSoilMap = this[1].split("\n").getAlmanacMap(),
                soilToFertilizerMap = this[2].split("\n").getAlmanacMap(),
                fertilizerToWaterMap = this[3].split("\n").getAlmanacMap(),
                waterToLightMap = this[4].split("\n").getAlmanacMap(),
                lightToTemperatureMap = this[5].split("\n").getAlmanacMap(),
                temperatureToHumidity = this[6].split("\n").getAlmanacMap(),
                humidityToLocationMap = this[7].split("\n").getAlmanacMap()
            )
        }

        fun List<String>.getAlmanacMap(): List<Pair<LongRange, LongRange>> {
            val map = mutableListOf<Pair<LongRange, LongRange>>()
            this.forEachIndexed { index, str ->
                if (index != 0) {
                    val numbers = str.getNumberList()
                    val destinationRange = numbers[0] until numbers[0] + numbers[2]
                    val sourceRange = numbers[1] until numbers[1] + numbers[2]
                    map.add(Pair(sourceRange, destinationRange))
                }
            }
            return map
        }
    }
}

