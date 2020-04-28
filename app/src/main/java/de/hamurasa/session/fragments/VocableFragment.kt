package de.hamurasa.session.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import de.hamurasa.R
import de.hamurasa.session.SessionContext
import de.hamurasa.session.SessionType
import de.hamurasa.session.SessionViewModel
import de.hamurasa.session.models.VocableWrapper
import de.hamurasa.settings.SettingsContext
import de.util.hamurasa.utility.AbstractFragment
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.vocable_session_fragment.*
import kotlinx.android.synthetic.main.vocable_session_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*

class VocableFragment : AbstractFragment(), View.OnClickListener {

    private val myViewModel: SessionViewModel by sharedViewModel()

    private lateinit var vocableFirstValueTextView: TextView

    private lateinit var vocableSecondValueTextView: TextView

    private lateinit var vocableProgressBar: ProgressBar

    override fun getLayoutId(): Int = R.layout.vocable_session_fragment

    override fun init(view: View) {
        vocableFirstValueTextView = view.findViewById(R.id.vocable_first_value)
        vocableSecondValueTextView = view.findViewById(R.id.vocable_second_value)
        vocableProgressBar = view.findViewById(R.id.vocableProgressBar)
        view.vocable_second_value.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData() {
        myViewModel.observe(SessionContext.activeVocable) {
            val value = when (SessionContext.sessionType) {
                SessionType.STANDARD -> it.value
                SessionType.ALTERNATIVE -> it.translation
            }
            vocableFirstValueTextView.text = value
            vocableProgressBar.progress =
                (it.level - 1) * (100 / SettingsContext.SessionSettings.maxRepetitions)

        }
    }

    override fun onClick(v: View?) {
        if (vocable_second_value != null) {
            val activeVocable = SessionContext.activeVocable.blockingFirst()

            val value = when (SessionContext.sessionType) {
                SessionType.STANDARD -> activeVocable.translation
                SessionType.ALTERNATIVE -> activeVocable.value
            }

            vocableSecondValueTextView.text = value
        }
    }

}