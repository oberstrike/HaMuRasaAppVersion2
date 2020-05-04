package de.hamurasa.main.fragments.dialogs

import android.os.Bundle
import android.view.View
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Language
import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.lesson.model.vocable.VocableType
import de.hamurasa.main.MainContext
import de.hamurasa.main.MainViewModel
import de.hamurasa.main.fragments.DictionaryFragment
import de.hamurasa.settings.SettingsContext
import de.hamurasa.util.isValid
import de.util.hamurasa.utility.*
import kotlinx.android.synthetic.main.dialog_new_vocable.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

//Reworked
class NewVocableDialog(private val vocable: Vocable) : AbstractDialog(), View.OnClickListener {

    private val myViewModel: MainViewModel by sharedViewModel()

    override fun getLayoutId(): Int = R.layout.dialog_new_vocable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vocable.id = 0

        new_vocable_value_editText.bind(vocable::value)
        new_vocable_translations_editText.bind(
            vocable::translation,
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

        new_vocable_offline_checkbox.setOnCheckedChangeListener { _, checked ->
            vocable.isOffline = checked
        }

        myViewModel.observe(SettingsContext.isOffline) {
            if (it) {
                new_vocable_offline_checkbox.isChecked = true
                new_vocable_offline_checkbox.isEnabled = false
            } else {
                new_vocable_offline_checkbox.isEnabled = true
            }
        }


        new_vocable_type_spinner.initAdapter<VocableType>()
        new_vocable_language_spinner.initAdapter<Language>()
    }


    override fun onClick(v: View?) {
        if (!vocable.isValid()) {
            activity?.toast("Please fill in all the information.")
            return
        }

        if (MainContext.activeFragment is DictionaryFragment) {
            myViewModel.addVocableToServer(vocable)
        } else {
            val activeLesson = MainContext.EditContext.lesson.blockingFirst()
            myViewModel.addVocableToLesson(vocable, activeLesson.id)
        }

        dismiss()
    }

}

