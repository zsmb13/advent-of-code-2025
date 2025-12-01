fun main() {
    fun part1(input: List<String>): Int {
        var dial = 50
        return input.count {
            dial = if (it[0] == 'L') {
                (dial - it.drop(1).toInt()) % 100
            } else {
                (dial + it.drop(1).toInt()) % 100
            }
            dial == 0
        }
    }

    fun part2(input: List<String>): Int {
        var dial = 50
        var zeroes = 0
        input.forEach {
            val delta = if (it[0] == 'L') -1 else 1

            repeat(it.drop(1).toInt()) {
                dial = (dial + delta) % 100
                if (dial == 0) zeroes++
            }
        }
        return zeroes
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 6)

    val input = readInput("Day01")
    check(part1(input) == 992)
    check(part2(input) == 6133)
}
