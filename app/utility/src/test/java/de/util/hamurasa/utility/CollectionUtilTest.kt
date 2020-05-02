package de.util.hamurasa.utility

import android.os.Build
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
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