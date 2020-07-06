package de.hamurasa.settings

import de.hamurasa.R
import de.hamurasa.main.MainViewModel
import de.util.hamurasa.utility.util.*
import kotlinx.android.synthetic.main.content_settings.*
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsActivity : AbstractActivity<MainViewModel>() {

    override val myViewModel: MainViewModel by viewModel()

    override val layoutRes: Int = R.layout.activity_settings

    override val toolbarToUse: androidx.appcompat.widget.Toolbar? = null

    override fun init() {
        maximum_vocable_count_editText.setText(SettingsContext.SessionSettings.maxVocableCount.toString())
        maximum_vocable_count_editText.afterTextChanged {
            if (it.isNotBlank() && it.isNotEmpty()) {
                val count = it.toInt()
                SettingsContext.SessionSettings.maxVocableCount = count
            }
        }

        number_of_repetitions_editText.setText(SettingsContext.SessionSettings.maxRepetitions.toString())
        number_of_repetitions_editText.afterTextChanged {
            if (it.isNotBlank() && it.isNotEmpty()) {
                val number = it.toInt()
                SettingsContext.SessionSettings.maxRepetitions = number
            }
        }

        setting_writing_type_checkBox.bind(SettingsContext.SessionSettings::writingInputType)

        setting_standard_type_checkBox.bind(SettingsContext.SessionSettings::standardInputType)

        setting_alternative_type_checkBox.bind(SettingsContext.SessionSettings::alternativeInputType)


    }


}