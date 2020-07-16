package de.hamurasa.session.fragments

import android.view.View
import de.hamurasa.R
import de.hamurasa.session.SessionViewModel
import de.hamurasa.session.models.VocableWrapper
import de.util.hamurasa.utility.util.afterTextChanged
import kotlinx.android.synthetic.main.vocable_session_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class WritingFragment(
    override val activeVocable: VocableWrapper,
    private val onTextChange: (String) -> Unit
) : BasicFragment(activeVocable) {

    override val myViewModel: SessionViewModel by sharedViewModel()

    var isPressed: Boolean = false

    override fun init() {
        session_vocable_second_value.setOnClickListener(this)

        session_vocable_first_value.text = activeVocable.translation

        session_value_editText.afterTextChanged(onTextChange)

        vocableProgressBar.progress =
            (this.activeVocable.level - 1) * (100 / settings.maxRepetitions)

    }

    override fun onClick(v: View?) {
        with(activeVocable) {
            session_vocable_second_value.text = this.value
            isPressed = true
        }
    }

    override fun reset() {
        session_vocable_second_value.text = resources.getText(R.string.show)
    }

}