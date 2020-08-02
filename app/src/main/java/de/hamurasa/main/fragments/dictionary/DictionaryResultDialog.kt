package de.hamurasa.main.fragments.dictionary

import android.os.Bundle
import android.view.View
import de.hamurasa.R
import de.hamurasa.data.vocable.Vocable
import de.hamurasa.main.MainContext
import de.hamurasa.util.BaseDialog
import de.hamurasa.util.toast
import de.hamurasa.util.widgets.afterSelectedChanged
import de.hamurasa.util.widgets.initAdapter
import kotlinx.android.synthetic.main.dialog_result.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.sharedViewModel

//Reworked
class DictionaryResultDialog(val vocable: Vocable) : BaseDialog(), View.OnClickListener {

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

        result_delete_button.setOnClickListener(this)


        myViewModel.launchJob {
            MainContext.HomeContext.flow.collect {
                val lessons = it.value?.lessons ?: return@collect
                val array = lessons.map { value -> value.id.toString() }.toTypedArray()
                result_lesson_Spinner.initAdapter(array = array)
            }
        }
    }


    @ExperimentalCoroutinesApi
    override fun onClick(v: View) {
        when (v.id) {
            R.id.result_delete_button -> {
                myViewModel.launchJob {
                    myViewModel.deleteVocable(vocable)
                }
            }
            else -> {
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


    }


}