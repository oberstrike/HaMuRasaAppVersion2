package de.hamurasa.main.fragments.dialogs

import android.view.View
import android.widget.Button
import de.hamurasa.R
import de.hamurasa.lesson.model.lesson.Lesson
import de.util.hamurasa.utility.AbstractDialog

class UpdateLessonDialog(lesson: Lesson) : AbstractDialog<Lesson>(lesson) {

    override fun getLayoutId(): Int = R.layout.dialog_update_lesson

    private lateinit var oldVersionButton: Button

    private lateinit var newVersionButton: Button

    override fun createView(view: View) {
        oldVersionButton = view.findViewById(R.id.update_lesson_version_one_button)
        newVersionButton = view.findViewById(R.id.update_lesson_version_two_button)
        oldVersionButton.setOnClickListener(this)
        newVersionButton.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.update_lesson_version_one_button -> println("old")
                R.id.update_lesson_version_two_button -> println("new")
            }
        }
    }


}