package de.hamurasa.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import de.hamurasa.R
import de.hamurasa.main.MainActivity
import de.hamurasa.main.MainContext
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


        login_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val username = username_editText.text.toString()
        val password = password_editText.text.toString()
        var loggedIn = false

        if (username.isNotEmpty() && username.isNotBlank()) {
            if (username.length > 6) {
                if (password.isNotBlank() && password.isNotBlank()) {
                    if (password.length > 6) {
                        loggedIn = loginViewModel.login(username, password)
                    }
                }
            }
        }

        if (loggedIn) {
            Toast.makeText(this, "Eingeloggt", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            MainContext.isLoggedIn = Observable.just(loggedIn)
            startActivity(intent)

        } else {
            Toast.makeText(this, "Falsche Eingabe", Toast.LENGTH_LONG).show()
        }
    }

}
