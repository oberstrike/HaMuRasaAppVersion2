package de.hamurasa.settings

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import de.hamurasa.R
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
        GlobalScope.launch(Dispatchers.IO) {
            init()
        }
    }


    private fun init() {
        initObserver()
        offline_switch.setOnCheckedChangeListener { _, isChecked ->
            SettingsContext.isOffline = BehaviorSubject.just(isChecked)
            SettingsContext.forceOffline = isChecked
            initObserver()
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