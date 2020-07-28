package de.util.hamurasa.utility

import de.hamurasa.util.weight
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CollectionUtilTest {

    @Test
    fun testWeightedList() {
        val pairList = listOf<Pair<Int, Int>>(
            1 to 2,
            2 to 3,
            3 to 4,
            4 to 5
        )


        val list = pairList.weight(keySelector = {
            it.first
        }, weightFunction = {
            pairList.size - it
        })

        val map = list.groupingBy { it.first }.eachCount()
        assertEquals(map[1], 4)
        assertEquals(map[2], 3)
        assertEquals(map[3], 2)
        assertEquals(map[4], 1)
    }
}