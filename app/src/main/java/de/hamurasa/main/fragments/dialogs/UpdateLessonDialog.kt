package de.hamurasa.main.fragments.dialogs

import android.view.View
import android.widget.Button
import de.hamurasa.R
import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.main.MainViewModel
import de.util.hamurasa.utility.AbstractDialog
import org.koin.android.viewmodel.ext.android.sharedViewModel

class UpdateLessonDialog(private val pair: Pair<Lesson, Lesson>) :
    AbstractDialog<Pair<Lesson, Lesson>>(pair) {

    private val myViewModel: MainViewModel by sharedViewModel()


    override fun getLayoutId(): Int = R.layout.dialog_update_lesson

    private lateinit var oldVersionButton: Button

    private lateinit var newVersionButton: Button

    override fun createView(view: View) {
        oldVersionButton = view.findViewById(R.id.update_lesson_version_one_button)
        oldVersionButton.text = pair.first.lastChanged.toString("dd.MM.yyyy HH:mm")

        newVersionButton = view.findViewById(R.id.update_lesson_version_two_button)
        newVersionButton.text = pair.second.lastChanged.toString("dd.MM.yyyy HH:mm")

        oldVersionButton.setOnClickListener(this)
        newVersionButton.setOnClickListener(this)

        isCancelable

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.update_lesson_version_one_button -> myViewModel.saveLesson(
                    pair.first,
                    pair.second.lastChanged
                )
                R.id.update_lesson_version_two_button -> myViewModel.saveLesson(pair.second)
            }
        }
        dismiss()
    }


}