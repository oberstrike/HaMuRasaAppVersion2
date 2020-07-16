package de.hamurasa.settings

import de.hamurasa.R
import de.hamurasa.main.MainViewModel
import de.hamurasa.settings.model.Settings
import de.util.hamurasa.utility.util.*
import kotlinx.android.synthetic.main.content_settings.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsActivity : AbstractActivity<MainViewModel>() {

    override val myViewModel: MainViewModel by viewModel()

    override val layoutRes: Int = R.layout.activity_settings

    override val toolbarToUse: androidx.appcompat.widget.Toolbar? = null

    val settings: Settings by inject()

    override fun init() {
        maximum_vocable_count_editText.setText(settings.maxVocableCount.toString())
        maximum_vocable_count_editText.afterTextChanged {
            if (it.isNotBlank() && it.isNotEmpty()) {
                val count = it.toInt()
                settings.maxVocableCount = count
            }
        }

        number_of_repetitions_editText.setText(settings.maxRepetitions.toString())
        number_of_repetitions_editText.afterTextChanged {
            if (it.isNotBlank() && it.isNotEmpty()) {
                val number = it.toInt()
                settings.maxRepetitions = number
            }
        }

        setting_writing_type_checkBox.bind(settings::writingType)

        setting_standard_type_checkBox.bind(settings::standardType)

        setting_alternative_type_checkBox.bind(settings::alternativeType)


    }


}