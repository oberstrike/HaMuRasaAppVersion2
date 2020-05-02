package de.hamurasa.main.fragments.dialogs

import android.os.Bundle
import android.view.View
import android.widget.Toast
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Language
import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.main.MainViewModel
import de.hamurasa.settings.SettingsContext
import de.hamurasa.util.isValid
import de.util.hamurasa.utility.*
import kotlinx.android.synthetic.main.dialog_new_lesson.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

//Reworked
class NewLessonDialog(private val lesson: Lesson) : AbstractDialog(), View.OnClickListener  {


    private val myViewModel: MainViewModel by sharedViewModel()

    override fun onClick(v: View?) {
        if (!lesson.isValid()) {
            activity!!.toast("The language have to be different")
            return
        }

        myViewModel.addLesson(lesson)
        val text = "The creation was successful!"
        val toast = Toast.makeText(activity, text, Toast.LENGTH_LONG)
        toast.show()
        dismiss()
    }

    override fun getLayoutId(): Int = R.layout.dialog_new_lesson

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        new_lesson_okButton.setOnClickListener(this)
        new_lesson_cancelButton.setOnClickListener {
            dismiss()
        }

        //Language Binding

        new_lesson_language_spinner.initAdapter<Language>()
        new_lesson_language_spinner.bind(lesson::language) {
            Language.valueOf(it)
        }

        //Validation Language Binding
        new_lesson_validation_language_spinner.initAdapter<Language>()
        new_lesson_validation_language_spinner.bind(lesson::validationLanguage) {
            Language.valueOf(it)
        }

        //Offline Binding
        new_lesson_offline_checkBox.bind(lesson::isOffline)
        myViewModel.observe(SettingsContext.isOffline) {
            if (it) {
                new_lesson_offline_checkBox.isChecked = true
                new_lesson_offline_checkBox.isEnabled = false
            } else {
                new_lesson_offline_checkBox.isEnabled = true
            }
        }

    }
}