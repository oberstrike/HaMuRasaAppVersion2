package de.hamurasa.main

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hamurasa.R
import de.hamurasa.login.LoginActivity
import de.hamurasa.main.fragments.DictionaryFragment
import de.hamurasa.main.fragments.HomeFragment
import de.hamurasa.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private val myViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    //    bottom_navigator.setOnNavigationItemSelectedListener(this)
        myViewModel.init()

      //  bottom_navigator.isEnabled = false
      //  bottom_navigator.isClickable = false

        loadFragment(HomeFragment())

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null)
            return super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.logout -> {
                myViewModel.logout()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_settings -> {

                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)

            }

        }


        return super.onOptionsItemSelected(item)
    }


    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        when (p0.itemId) {
            /*
            R.id.nav_search -> {
                if (MainContext.activeFragment !is DictionaryFragment) {
                    loadFragment(DictionaryFragment())
                    return true
                }
            }
            R.id.nav_home -> {
                if (MainContext.activeFragment !is HomeFragment) {
                    loadFragment(HomeFragment())
                    return true
                }
            }*/

        }
        return false
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        MainContext.activeFragment = fragment
    }
}
