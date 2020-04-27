package de.hamurasa.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.pwittchen.swipe.library.rx2.Swipe
import com.github.pwittchen.swipe.library.rx2.SwipeEvent
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Language
import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.login.LoginActivity
import de.hamurasa.main.fragments.*
import de.hamurasa.main.fragments.dialogs.NewLessonDialog
import de.hamurasa.main.fragments.dialogs.NewVocableDialog
import de.hamurasa.network.requestAsync
import de.hamurasa.settings.SettingsActivity
import de.hamurasa.settings.SettingsContext
import de.hamurasa.settings.model.Settings
import de.util.hamurasa.utility.findFirst
import de.util.hamurasa.utility.foreach
import de.util.hamurasa.utility.toast
import de.util.hamurasa.utility.withDialog
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.joda.time.DateTime
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private val myViewModel: MainViewModel by viewModel()

    private val settings: Settings by inject()

    private var loadingDialog: AlertDialog? = null

    private lateinit var swipe: Swipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        bottom_navigator.setOnNavigationItemSelectedListener(this)
        SettingsContext.init(settings)

        MainContext.EditContext.lesson = BehaviorSubject.create()


        swipe = Swipe(60, 300)
        checkConnection()


        requestAsync {
            init()
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
                            validationLanguage = Language.GER,
                            lastChanged = DateTime.now()
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
                    if (!SettingsContext.isOffline.blockingFirst()) {
                        loadFragment(DictionaryFragment())
                        return true
                    }

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
            val oldMenuItem = toolbar.menu.findFirst { each -> each.itemId == 1 }
            if (oldMenuItem == null) {
                toolbar.menu.add(Menu.NONE, 1, Menu.NONE, "Add Vocable")
                val menuItem = toolbar.menu.findItem(1)
                menuItem.setOnMenuItemClickListener {
                    val dialog: NewVocableDialog by inject()

                    dialog.show(supportFragmentManager, "New Vocable")
                    true
                }
            }
            if (fragment::class.java == DictionaryFragment::class.java) {
                modeSpinner.visibility = View.VISIBLE
            } else {
                modeSpinner.visibility = View.INVISIBLE
            }
        } else {
            toolbar.menu.removeItem(1)
            modeSpinner.visibility = View.INVISIBLE
        }
    }

    private fun checkLoggedIn() {
        myViewModel.observe(myViewModel.isLoggedIn) {
            if (!it) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                myViewModel.init()
                //       myViewModel.updateHome(false)

            }
        }
    }

    private fun checkConnection() {
        loadingDialog = withDialog(R.layout.dialog_loading).create()

        requestAsync(action = {
            myViewModel.checkConnection()
        }, onSuccess = {
            with(SettingsContext.activeLessonId) {
                if (this != 0) {
                    myViewModel.setActiveLesson(this.toLong())
                }
            }

            runBlocking {
                withContext(Dispatchers.Main) {
                    loadingDialog?.dismiss()
                }
            }

            myViewModel.observe(SettingsContext.isOffline) {
                myViewModel.init()
                if (it) toast("You are in offline mode.")
                else checkLoggedIn()
                loadFragment(HomeFragment())
            }
        })

        loadingDialog?.show()
    }

    private fun init() {

        myViewModel.observe(SettingsContext.isOffline) {
            bottom_navigator.menu.foreach { menuItem ->
                if (menuItem.itemId == R.id.nav_search) {
                    menuItem.isEnabled = !it
                }
            }
        }

        myViewModel.observe(MainContext.EditContext.lesson) {
            println(it)

        }

        myViewModel.observe(swipe.observe()) {
            val activeFragment = MainContext.activeFragment
            var newFragment: Fragment? = null

            if (it == SwipeEvent.SWIPED_RIGHT) {
                newFragment = when (activeFragment::class) {
                    HomeFragment::class -> EditFragment()
                    EditFragment::class -> if (SettingsContext.isOffline.blockingFirst()) HomeFragment() else DictionaryFragment()
                    DictionaryFragment::class -> HomeFragment()
                    else -> HomeFragment()
                }
            } else if (it == SwipeEvent.SWIPED_LEFT) {
                newFragment = when (activeFragment::class) {
                    HomeFragment::class -> if (SettingsContext.isOffline.blockingFirst()) EditFragment() else DictionaryFragment()
                    EditFragment::class -> HomeFragment()
                    DictionaryFragment::class -> EditFragment()
                    else -> HomeFragment()
                }

            }
            if (newFragment != null) {
                val newSelectedItem = when (newFragment) {
                    is HomeFragment -> R.id.nav_home
                    is DictionaryFragment -> R.id.nav_search
                    else -> R.id.nav_edit_lesson
                }
                bottom_navigator.selectedItemId = newSelectedItem

                loadFragment(newFragment)

            }
        }
    }


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        swipe.dispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }

    override fun onStop() {
        super.onStop()
        if (MainContext.EditContext.lesson.hasValue()) {
            val id = MainContext.EditContext.lesson.value?.serverId?.toInt()
            if (id != null) {
                SettingsContext.activeLessonId = id
            }
        }
        val lessons = MainContext.HomeContext.lessons.blockingFirst()
        for (lesson in lessons) {
            myViewModel.saveLesson(lesson)
        }

    }

}
