/**
 * Splits a string on and includes delimiters in the result.
 */
fun String.splitAll(regex: Regex): List<String> {
    val ranges = regex.findAll(this)
        .map { it.range }
        .sortedBy { it.first }
        .toList()

    return if (ranges.isEmpty()) listOf(this) else buildList {
        add(0 until ranges.first().first)
        addAll(
            ranges.fold(listOf<IntRange>()) { acc, range ->
                if (acc.isEmpty()) {
                    listOf(range)
                } else {
                    acc
                        .plusElement(((acc.last().last + 1) until range.first))
                        .plusElement(range)
                }
            }
        )
        add(ranges.last().last + 1 until length)
    }.mapNotNull {
        if (it.isEmpty()) null else substring(it)
    }
}
