package de.hamurasa


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import de.hamurasa.main.MainActivity
import de.hamurasa.data.lesson.LessonService
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.inject

/**
 * Basic tests showcasing simple view matchers and actions like [ViewMatchers.withId],
 * [ViewActions.click] and [ViewActions.typeText].
 *
 *
 * Note that there is no need to tell Espresso that a view is in a different [Activity].
 */
/*
@RunWith(AndroidJUnit4::class)
class ChangeTextBehaviorTest : KoinTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    val lessonService: LessonService by inject()

    @Test
    fun changeText_newActivity() {
        val context = InstrumentationRegistry.getInstrumentation().context


        openActionBarOverflowOrOptionsMenu(
            context
        )

        onView(
            withId(R.id.action_new_lesson)
        ).perform(click())

    }

    companion object {
        const val STRING_TO_BE_TYPED = "Espresso"
    }
}*/