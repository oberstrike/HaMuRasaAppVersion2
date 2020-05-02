package de.util.hamurasa.utility

inline fun <E> Collection<E>.weight(
    crossinline keySelector: (E) -> Int,
    crossinline weightFunction: (Int) -> Int
): List<E> {
    val tmpList = mutableListOf<E>()
    for (entry in groupBy(keySelector)) {
        val count = weightFunction.invoke(entry.key)
        for (entity in entry.value) {
            for (i in 0..count) {
                tmpList.add(entity)
            }
        }

    }
    return tmpList.toList()
}