package de.hamurasa.main.fragments.dialogs

import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Language
import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.main.MainViewModel
import de.hamurasa.settings.SettingsContext
import de.hamurasa.util.isValid
import de.util.hamurasa.utility.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

//Reworked
class NewLessonDialog(private val lesson: Lesson) : AbstractDialog<Lesson>(lesson) {

    private lateinit var newLanguageSpinner: Spinner

    private lateinit var newValidationLanguageSpinner: Spinner

    private lateinit var addButton: Button

    private lateinit var cancelButton: Button

    private lateinit var istOfflineCheckBox: CheckBox

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

    override fun createView(view: View) {
        addButton = view.findViewById(R.id.okButton)
        addButton.setOnClickListener(this)

        cancelButton = view.findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            dismiss()
        }

        newLanguageSpinner = view.findViewById(R.id.new_language_spinner)
        activity!!.createSpinner<Language>(newLanguageSpinner)
        newLanguageSpinner.afterSelectedChanged {
            val language = Language.valueOf(it)
            lesson.language = language
        }


        newValidationLanguageSpinner = view.findViewById(R.id.new_validation_language_spinner)
        activity!!.createSpinner<Language>(newValidationLanguageSpinner)
        newValidationLanguageSpinner.afterSelectedChanged {
            val validationLanguage = Language.valueOf(it)
            lesson.validationLanguage = validationLanguage
        }

        istOfflineCheckBox = view.findViewById(R.id.new_lesson_offline_checkBox)
        istOfflineCheckBox.setOnCheckedChangeListener { _, checked ->
            lesson.isOffline = checked
        }

        myViewModel.observe(SettingsContext.isOffline){
            if(it){
                istOfflineCheckBox.isChecked = true
                istOfflineCheckBox.isEnabled = false
            }else{
                istOfflineCheckBox.isEnabled = true
            }
        }
    }


}