package de.hamurasa.main.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.main.MainContext
import de.hamurasa.main.MainViewModel
import de.util.hamurasa.utility.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

//Reworked
class ResultAlertDialog(val vocable: Vocable) : AbstractDialog<Vocable>(vocable) {

    private var lessonId: Long = 0

    private lateinit var translationTextView: TextView

    private lateinit var resultLessonSpinner: Spinner

    private lateinit var addToLessonButton: Button

    private val myViewModel: MainViewModel by sharedViewModel()

    override fun getLayoutId(): Int = R.layout.dialog_result

    override fun createView(view: View) {
        addToLessonButton = view.findViewById(R.id.add_to_lesson_button)
        addToLessonButton.setOnClickListener(this)

        translationTextView = view.findViewById(R.id.translation_text_view)
        translationTextView.text = vocable.translation.toString()

        resultLessonSpinner = view.findViewById(R.id.result_lesson_Spinner)
        resultLessonSpinner.afterSelectedChanged {
            lessonId = it.toLong()
        }

        val array = MainContext.HomeContext.lessons.blockingFirst().map { it.serverId.toString() }.toTypedArray()
        activity!!.createSpinner(resultLessonSpinner, array = array)

    }


    override fun onClick(v: View?) {
        if (lessonId != 0L) {
            myViewModel.addVocableToLesson(vocable, lessonId)
            dismiss()
        } else {
            activity!!.toast("There was an error.")
        }
    }


}