package de.hamurasa.main.fragments.dialogs

import android.os.Bundle
import android.view.View
import de.hamurasa.R
import de.hamurasa.data.vocable.Language
import de.hamurasa.data.vocable.Vocable
import de.hamurasa.data.vocable.VocableType
import de.hamurasa.util.BaseDialog
import de.hamurasa.util.BaseViewModel
import de.hamurasa.util.isValid
import de.hamurasa.util.toast
import de.hamurasa.util.widgets.afterSelectedChanged
import de.hamurasa.util.widgets.bind
import de.hamurasa.util.widgets.initAdapter
import kotlinx.android.synthetic.main.dialog_new_vocable.*

abstract class BaseNewVocableDialog(
    private val vocable: Vocable
) : BaseDialog(),
    View.OnClickListener {

    protected abstract val myViewModel: BaseViewModel

    override fun getLayoutId(): Int = R.layout.dialog_new_vocable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vocable.id = 0

        new_vocable_value_editText.bind(vocable::value)
        new_vocable_translations_editText.bind(
            maxLength = 25,
            property = vocable::translation,
            toString = { list: List<String> ->
                if (list.isNotEmpty()) list.reduce { a, b -> "$a,$b" } else ""
            }) {
            it.split(",")
        }

        new_vocable_add_button.setOnClickListener(this)

        new_vocable_type_spinner.afterSelectedChanged {
            val type = VocableType.valueOf(it)
            vocable.type = type
        }

        new_vocable_language_spinner.afterSelectedChanged {
            val language = Language.valueOf(it)
            vocable.language = language
        }



        new_vocable_type_spinner.initAdapter<VocableType>()
        new_vocable_language_spinner.initAdapter<Language>()
    }

    override fun onClick(v: View?) {
        if (!vocable.isValid()) {
            activity?.toast("Please fill in all the information.")
            return
        }
        onAddVocable()
        dismiss()
    }


    abstract fun onAddVocable()
}
