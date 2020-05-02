package de.hamurasa.main.fragments.edit

import android.os.Bundle
import android.view.View
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.lesson.model.vocable.VocableDTO
import de.hamurasa.lesson.model.vocable.VocableType
import de.hamurasa.main.MainViewModel
import de.hamurasa.util.isValid
import de.util.hamurasa.utility.*
import kotlinx.android.synthetic.main.dialog_edit_vocable.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


//Reworked
class EditVocableDialog(val vocable: Vocable) : AbstractDialog(), View.OnClickListener  {

    private val myViewModel: MainViewModel by sharedViewModel()

    override fun getLayoutId(): Int = R.layout.dialog_edit_vocable


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        valueEditText.bind(vocable::value)

        translationEditText.setText(if (vocable.translation.isNotEmpty()) vocable.translation.reduce { acc, s -> "$acc,$s" } else "")
        translationEditText.afterTextChanged {
            if (it.isNotBlank()) vocable.translation = it.split(",")
        }



        editVocableOfflineCheckBox.isEnabled = !vocable.isOffline
        editVocableOfflineCheckBox.bind(vocable::isOffline)

        applyButton.setOnClickListener(this)

        editVocableTypeSpinner.initAdapter<VocableType>()
        editVocableTypeSpinner.bind(vocable::type){
            VocableType.valueOf(it)
        }


        deleteButton.setOnClickListener {
            delete()
        }
    }

    override fun onClick(v: View?) {
        if (!vocable.isValid()) {
            activity!!.toast("Please fill all required forms")
            return
        }

        myViewModel.patchVocable(vocable)
        dismiss()
    }

    private fun delete() {
        val isOffline = editVocableOfflineCheckBox.isChecked
        val id = if (!isOffline) vocable.serverId else vocable.id

        val vocableDTO = VocableDTO.create(
            id,
            vocable.value,
            vocable.type,
            vocable.translation,
            vocable.language
        )
        myViewModel.deleteVocableFromLesson(vocableDTO, isOffline)
        dismiss()
    }
}
