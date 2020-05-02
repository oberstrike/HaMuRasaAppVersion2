package de.hamurasa.main.fragments.dialogs

import android.os.Bundle
import android.view.View
import de.hamurasa.R
import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.main.MainViewModel
import de.util.hamurasa.utility.AbstractDialog
import kotlinx.android.synthetic.main.dialog_update_lesson.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class UpdateLessonDialog(private val pair: Pair<Lesson, Lesson>) : AbstractDialog(),
    View.OnClickListener {

    private val myViewModel: MainViewModel by sharedViewModel()


    override fun getLayoutId(): Int = R.layout.dialog_update_lesson

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update_lesson_old_version_button.text = pair.first.lastChanged.toString("dd.MM.yyyy HH:mm")
        update_lesson_new_version__button.text =
            pair.second.lastChanged.toString("dd.MM.yyyy HH:mm")
        update_lesson_old_version_button.setOnClickListener(this)
        update_lesson_new_version__button.setOnClickListener(this)
        isCancelable = false
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.update_lesson_old_version_button -> myViewModel.saveLesson(
                    pair.first,
                    pair.second.lastChanged
                )
                R.id.update_lesson_new_version__button -> myViewModel.saveLesson(pair.second)
            }
        }
        dismiss()
    }


}