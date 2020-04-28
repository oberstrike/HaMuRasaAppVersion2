package de.hamurasa.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.hamurasa.R
import de.util.hamurasa.utility.afterTextChanged
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.content_settings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val myViewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        init()
    }

    private fun init() {
        initObserver()
        offline_switch.setOnCheckedChangeListener { _, isChecked ->
            SettingsContext.isOffline = Observable.just(isChecked)
            SettingsContext.forceOffline = isChecked
            initObserver()
        }
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


    }

    private fun initObserver() {
        myViewModel.launch {
            SettingsContext.isOffline
                .subscribeOn(myViewModel.provider.computation())
                .observeOn(myViewModel.provider.ui())
                .subscribe {
                    offline_switch.isChecked = it
                }
        }
    }


}