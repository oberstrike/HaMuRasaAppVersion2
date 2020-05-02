package de.util.hamurasa.utility

import android.app.Activity
import android.os.Build
import android.widget.LinearLayout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ExampleUnitTest {


    private val activity = Robolectric.buildActivity(Activity::class.java)

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_observer_with_interface() {
        val context = activity.get().applicationContext
        val layout = LinearLayout(context)

        assertNotNull(layout)
    }


}
