package de.hamurasa.main.fragments.edit

import android.graphics.Color
import android.os.Bundle
import android.view.View
import de.hamurasa.R
import de.hamurasa.data.vocable.Vocable
import de.hamurasa.main.MainContext
import de.hamurasa.util.*
import de.hamurasa.util.widgets.afterTextChanged
import de.hamurasa.util.widgets.bind
import de.hamurasa.util.widgets.initAdapter
import kotlinx.android.synthetic.main.dialog_edit_vocable.*
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


//Reworked
class EditVocableDialog(
    val vocable: Vocable
) :
    BaseDialog(), View.OnClickListener {

    private val myViewModel: EditViewModel by sharedViewModel()

    override fun getLayoutId(): Int = R.layout.dialog_edit_vocable


    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        valueEditText.bind(vocable::value)

        translationEditText.setText(if (vocable.translation.isNotEmpty()) vocable.translation.reduce { acc, s -> "$acc,$s" } else "")
        translationEditText.afterTextChanged {
            if (it.isNotBlank()) vocable.translation = it.split(",")
        }

        GlobalScope.launch(Dispatchers.IO) {
            val stats = myViewModel.stats(vocable) ?: return@launch
            withContext(Dispatchers.Main) {
                val color =
                    if (stats.repetitions == 0L) Color.RED else if (stats.repetitions < 5) Color.YELLOW else Color.GREEN
                valueEditText.setTextColor(color)
            }
        }

        applyButton.setOnClickListener(this)

        editVocableTypeSpinner.initAdapter<de.hamurasa.data.vocable.VocableType>()
        editVocableTypeSpinner.bind(vocable::type) {
            de.hamurasa.data.vocable.VocableType.valueOf(it)
        }


        editVocableLastTimeLearnedEditText.setText( vocable.lastChanged.toString( "dd.MM.yyyy hh:mm.ss" ) )
        editVocableLastTimeLearnedEditText.isEnabled = false

        deleteButton.setOnClickListener {
            delete()
        }
    }

    @ExperimentalCoroutinesApi
    override fun onClick(v: View?) {
        if (!vocable.isValid()) {
            requireActivity().toast("Please fill all required forms")
            return
        }

        myViewModel.launchJob {
            myViewModel.patchVocable(vocable)
            withContext(Dispatchers.Main) {
                dismiss()
            }
        }

    }

    @ExperimentalCoroutinesApi
    private fun delete() {
        val lesson = MainContext.EditContext.value() ?: return

        myViewModel.launchJob {
            myViewModel.deleteVocableFromLesson(
                vocable,
                lesson
            )
            dismiss()
            //     withContext(Dispatchers.Main) {
            //         editFragment.updateLesson(lesson)
            //         dismiss()
            //    }
        }
    }
}
