package de.hamurasa.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Language
import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.login.LoginActivity
import de.hamurasa.main.fragments.*
import de.hamurasa.main.fragments.dialogs.LoadingDialog
import de.hamurasa.main.fragments.dialogs.LoadingHandler
import de.hamurasa.main.fragments.dialogs.NewLessonDialog
import de.hamurasa.main.fragments.dialogs.NewVocableDialog
import de.hamurasa.settings.SettingsActivity
import de.hamurasa.settings.SettingsContext
import de.hamurasa.settings.model.Settings
import de.hamurasa.util.foreach
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private val myViewModel: MainViewModel by viewModel()

    private val settings: Settings by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        bottom_navigator.setOnNavigationItemSelectedListener(this)
        SettingsContext.init(settings)
        MainContext.EditContext.lesson = BehaviorSubject.create()

        checkConnection()

        myViewModel.launch {
            SettingsContext.isOffline.subscribeOn(myViewModel.provider.computation())
                .observeOn(myViewModel.provider.ui())
                .subscribe {
                    bottom_navigator.menu.foreach { menuItem ->
                        if (menuItem.itemId == R.id.nav_search) {
                            menuItem.isEnabled = it
                        }

                    }
                }
        }


    }

    override fun onResume() {
        super.onResume()
        bottom_navigator.menu.foreach {
            if (it.itemId == R.id.nav_search) {
                it.isEnabled = !SettingsContext.forceOffline
            }
        }
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
            R.id.action_new_lesson -> {
                val fragment =
                    NewLessonDialog(
                        Lesson(
                            0,
                            language = Language.ES,
                            validationLanguage = Language.GER
                        )
                    )
                fragment.show(supportFragmentManager, "New Lesson")
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_search -> {
                if (MainContext.activeFragment !is DictionaryFragment) {
                    loadFragment(DictionaryFragment())
                    return true
                }
            }
            R.id.nav_edit_lesson -> {
                if (MainContext.activeFragment !is EditFragment) {
                    loadFragment(EditFragment())
                    return true
                }

            }

            R.id.nav_home -> {
                if (MainContext.activeFragment !is HomeFragment) {
                    loadFragment(HomeFragment())
                    return true
                }
            }
        }
        return false
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        MainContext.activeFragment = fragment

        if (fragment::class.java == DictionaryFragment::class.java || fragment::class.java == EditFragment::class.java) {
            toolbar.menu.add(Menu.NONE, 1, Menu.NONE, "Add Vocable");
            val menuItem = toolbar.menu.findItem(1)
            menuItem.setOnMenuItemClickListener {
                val dialog =
                    NewVocableDialog()
                dialog.show(supportFragmentManager, "New Vocable")
                true
            }
            modeSpinner.visibility = View.VISIBLE
        } else {
            toolbar.menu.removeItem(1)
            modeSpinner.visibility = View.INVISIBLE
        }
    }

    fun checkLoggedIn() {
        myViewModel.launch {
            myViewModel.isLoggedIn
                .takeLast(1)
                .subscribeOn(myViewModel.provider.computation())
                .observeOn(myViewModel.provider.ui())
                .subscribe {
                    if (!it) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {

                        myViewModel.update()
                    }
                }
        }
    }

    private fun checkConnection() {
        val handler = object :
            LoadingHandler {
            override suspend fun process() {
                myViewModel.checkConnection()
            }

            override suspend fun onProcessFinished() {
                myViewModel.launch {
                    SettingsContext.isOffline.subscribeOn(myViewModel.provider.computation())
                        .observeOn(myViewModel.provider.ui())
                        .subscribe {
                            if (it) {
                                val toast = Toast.makeText(
                                    this@MainActivity,
                                    "You are in offline mode. ",
                                    Toast.LENGTH_LONG
                                )
                                toast.show()
                            } else {
                                myViewModel.init()
                                checkLoggedIn()

                            }
                            loadFragment(HomeFragment())
                        }
                }


            }

        }

        val loadingDialog =
            LoadingDialog(handler)
        loadingDialog.show(supportFragmentManager, "Test")
    }

}
