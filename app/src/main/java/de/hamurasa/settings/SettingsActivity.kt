package de.hamurasa.settings

import android.os.Bundle
import com.mitteloupe.solid.activity.handler.LifecycleHandler
import de.hamurasa.R
import de.hamurasa.main.MainViewModel
import de.hamurasa.settings.model.Settings
import de.hamurasa.util.BaseActivity
import de.hamurasa.util.widgets.afterTextChanged
import de.hamurasa.util.widgets.bind
import de.hamurasa.util.widgets.bindInt
import kotlinx.android.synthetic.main.content_settings.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsInitHandler(private val activity: SettingsActivity) : LifecycleHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(activity) {
            settings_maximum_vocable_count_in_lesson_editText.bindInt(settings::maxVocableCount)
            settings_maximum_lesson_per_page_editText.bindInt(settings::maxLessonsPerPage)
            settings_maximum_vocables_per_page_editText.bindInt(settings::maxVocablesPerPage)
            settings_number_of_repetitions_editText.bindInt(settings::maxRepetitions)
            setting_writing_type_checkBox.bind(settings::writingType)
            setting_standard_type_checkBox.bind(settings::standardType)
            setting_alternative_type_checkBox.bind(settings::alternativeType)
        }
    }
}

class SettingsActivity :
    BaseActivity<MainViewModel>() {

    override val lifecycleHandlers: List<LifecycleHandler> = listOf(SettingsInitHandler(this))

    override val myViewModel: MainViewModel by viewModel()

    override val layoutRes: Int = R.layout.activity_settings

    override val toolbarToUse: androidx.appcompat.widget.Toolbar? = null

    val settings: Settings by inject()
}