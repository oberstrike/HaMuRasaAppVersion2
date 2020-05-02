package de.hamurasa.main.fragments.dialogs

import android.os.Bundle
import android.view.View
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.main.MainContext
import de.hamurasa.main.MainViewModel
import de.util.hamurasa.utility.*
import kotlinx.android.synthetic.main.dialog_result.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

//Reworked
class ResultAlertDialog(val vocable: Vocable) : AbstractDialog(), View.OnClickListener  {

    private var lessonId: Long = 0


    private val myViewModel: MainViewModel by sharedViewModel()

    override fun getLayoutId(): Int = R.layout.dialog_result

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        result_add_to_lesson_button.setOnClickListener(this)

        result_translation_textView.text = vocable.translation.toString()

        result_lesson_Spinner.afterSelectedChanged {
            lessonId = it.toLong()
        }

        val array = MainContext.HomeContext.lessons.blockingFirst().map { it.serverId.toString() }
            .toTypedArray()
        result_lesson_Spinner.initAdapter(array = array)
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