package de.hamurasa.main.fragments.dictionary

import android.os.Bundle
import android.view.View
import de.hamurasa.R
import de.hamurasa.model.vocable.Vocable
import de.hamurasa.main.MainContext
import de.util.hamurasa.utility.util.AbstractDialog
import de.util.hamurasa.utility.util.afterSelectedChanged
import de.util.hamurasa.utility.util.initAdapter
import de.util.hamurasa.utility.util.toast
import kotlinx.android.synthetic.main.dialog_result.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import org.koin.android.viewmodel.ext.android.sharedViewModel

//Reworked
class ResultAlertDialog(val vocable: Vocable) : AbstractDialog(), View.OnClickListener {

    private var lessonId: Long = 0

    private val myViewModel: DictionaryViewModel by sharedViewModel()

    override fun getLayoutId(): Int = R.layout.dialog_result

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        result_add_to_lesson_button.setOnClickListener(this)

        result_translation_textView.text = vocable.translation.toString()

        result_lesson_Spinner.afterSelectedChanged {
            lessonId = it.toLong()
        }


        myViewModel.launchJob {
            MainContext.HomeContext.lessons.collect {
                if (it == null)
                    return@collect
                val array = it.map { value -> value.id.toString() }.toTypedArray()
                result_lesson_Spinner.initAdapter(array = array)
            }
        }
    }


    @ExperimentalCoroutinesApi
    override fun onClick(v: View?) {
        if (lessonId != 0L) {
            myViewModel.launchJob {
                myViewModel.addVocableToLesson(vocable, lessonId)
                withContext(Dispatchers.Main) {
                    dismiss()
                }
            }
        } else {
            requireActivity().toast("There was an error.")
        }
    }


}