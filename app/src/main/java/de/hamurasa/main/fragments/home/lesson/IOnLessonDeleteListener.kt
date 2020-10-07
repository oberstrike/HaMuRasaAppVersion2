package de.hamurasa.main.fragments.home.lesson

import androidx.fragment.app.FragmentManager
import de.hamurasa.data.lesson.Lesson

interface IOnLessonDeleteListener {
    fun onDelete(lesson: Lesson, fragmentManager: FragmentManager)
}


class OnLessonDeleteListenerImpl : IOnLessonDeleteListener{
    override fun onDelete(lesson: Lesson, fragmentManager: FragmentManager) {
        val deleteLessonDialog = DeleteLessonDialog(lesson)
        deleteLessonDialog.show(fragmentManager, "Delete")
    }
}