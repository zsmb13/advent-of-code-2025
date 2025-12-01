import kotlin.math.sign

fun main() {
    fun part1(input: List<String>): Int {
        var dial = 50
        var zeroes = 0
        input.forEach {
            if (it[0] == 'L') {
                dial = (dial - it.drop(1).toInt()) % (100)
            } else {
                dial = (dial + it.drop(1).toInt()) % (100)
            }
            if (dial == 0) zeroes++
        }
        return zeroes
    }

//    fun part2(input: List<String>): Int {
//        var dial = 50
//        var zeroes = 0
//        input.forEach {
//            val sign = dial.sign
//            if (it[0] == 'L') {
//                dial = (dial - it.drop(1).toInt())
//            } else {
//                dial = (dial + it.drop(1).toInt())
//            }
//
//            var fromZero = sign == 0
//
//            if (dial == 100) {
//                dial = 0
//                println("COUNT dial is 0")
//                zeroes++
//            } else if (dial == 0) {
//                println("COUNT dial is 0")
//                zeroes++
//            }
//
//            while (dial < 0) {
//                if (fromZero) {
//                    fromZero = false
//                } else {
//                    zeroes ++
//                }
//                println("COUNT dial is now $dial, adding 100")
//                dial += 100
//            }
//            while (dial > 100) {
//                if (fromZero) {
//                    fromZero = false
//                } else {
//                    zeroes ++
//                }
//                println("COUNT dial is now $dial, subbing 100")
//                dial -= 100
//            }
//
//            println("dial adjusted is $dial")
//        }
//        return zeroes
//    }

    fun part2(input: List<String>): Int {
        var dial = 50
        var zeroes = 0
        input.forEach {
            val delta = if (it[0] == 'L') -1 else 1

            repeat(it.drop(1).toInt()) {
                dial = (dial + delta) % 100
                if (dial == 0) zeroes++
            }

            println("dial is $dial")
        }
        return zeroes
    }

    val testInput = readInput("Day01_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
