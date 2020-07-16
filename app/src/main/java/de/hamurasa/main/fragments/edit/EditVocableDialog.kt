package de.hamurasa.main.fragments.edit

import android.os.Bundle
import android.view.View
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.model.vocable.Vocable
import de.hamurasa.model.vocable.VocableDTO
import de.hamurasa.model.vocable.VocableType
import de.hamurasa.util.isValid
import de.util.hamurasa.utility.util.*
import kotlinx.android.synthetic.main.dialog_edit_vocable.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.android.viewmodel.ext.android.sharedViewModel


//Reworked
class EditVocableDialog(val vocable: Vocable) :
    AbstractDialog(), View.OnClickListener {

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


        applyButton.setOnClickListener(this)

        editVocableTypeSpinner.initAdapter<VocableType>()
        editVocableTypeSpinner.bind(vocable::type) {
            VocableType.valueOf(it)
        }


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
        val id = vocable.id

        val vocableDTO = VocableDTO.create(
            id,
            vocable.value,
            vocable.type,
            vocable.translation,
            vocable.language
        )
        val lesson = MainContext.EditContext.lesson.value ?: return

        myViewModel.launchJob {
            myViewModel.deleteVocableFromLesson(
                vocableDTO,
                lesson
            )
            withContext(Dispatchers.Main) {
                dismiss()
            }
        }
    }
}
