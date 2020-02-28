package de.hamurasa.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.hamurasa.R
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val myViewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}