package de.hamurasa.session.fragments

import android.view.View
import de.hamurasa.R
import de.hamurasa.session.SessionViewModel
import de.hamurasa.session.models.VocableWrapper
import de.hamurasa.settings.model.Settings
import kotlinx.android.synthetic.main.vocable_session_fragment.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

open class ClickingFragment(
    override val activeVocable: VocableWrapper,
    private val firstValueConverter: (VocableWrapper) -> String,
    private val secondValueConverter: (VocableWrapper) -> String
) : BasicFragment(activeVocable) {

    override val myViewModel: SessionViewModel by sharedViewModel()




    override fun init() {
        session_vocable_second_value.setOnClickListener(this)

        session_value_editText.visibility = View.INVISIBLE

        session_vocable_first_value.text = firstValueConverter.invoke(activeVocable)

        vocableProgressBar.progress =
            (this.activeVocable.level - 1) * (100 / settings.maxRepetitions)

    }


    override fun onClick(v: View?) {
        session_vocable_second_value.text = secondValueConverter.invoke(activeVocable)
    }

    override fun reset() {
        session_vocable_first_value.text = firstValueConverter.invoke(activeVocable)
        session_vocable_second_value.text = resources.getText(R.string.show)
    }
}

class StandardFragment(
    activeVocable: VocableWrapper
) : ClickingFragment(
    activeVocable,
    { it.value },
    { it.translation }
)

class AlternativeFragment(
    activeVocable: VocableWrapper
) : ClickingFragment(
    activeVocable,
    { it.translation },
    { it.value }
)