package de.hamurasa.main.fragments.home.lesson

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import de.hamurasa.data.lesson.Lesson
import de.hamurasa.session.SessionActivity
import de.hamurasa.session.models.SessionEvent
import de.hamurasa.session.models.VocableWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi

class OnSessionStartListener(
    private val sessionEvent: SessionEvent,
) : IOnSessionStartListener {

    @ExperimentalCoroutinesApi
    override fun onLessonStarted(lesson: Lesson, activity: FragmentActivity) {
        if (lesson.words.isEmpty())
            return

        sessionEvent.vocables = lesson.words

        val intent = Intent(activity, SessionActivity::class.java)
        activity.startActivity(intent)
    }
}

interface IOnSessionStartListener {
    fun onLessonStarted(lesson: Lesson, activity: FragmentActivity)
}