package de.hamurasa.main

import android.app.*
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.github.pwittchen.swipe.library.rx2.Swipe
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mitteloupe.solid.activity.handler.LifecycleHandler
import de.hamurasa.R
import de.hamurasa.main.fragments.adapters.OnLessonClickListener
import de.hamurasa.main.fragments.dialogs.ImportExportDialog
import de.hamurasa.main.fragments.dialogs.NewProfileDialog
import de.hamurasa.main.fragments.dictionary.DictionaryFragment
import de.hamurasa.main.fragments.edit.EditFragment
import de.hamurasa.main.fragments.edit.NewVocableDialog
import de.hamurasa.main.fragments.home.HomeFragment
import de.hamurasa.main.fragments.home.NewLessonDialog

import de.hamurasa.data.lesson.Lesson
import de.hamurasa.settings.SettingsActivity
import de.hamurasa.settings.model.Settings
import de.hamurasa.util.BaseSwipeActivity
import de.hamurasa.service.NotificationService
import de.hamurasa.util.findFirst
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MainInitHandler(private val mainActivity: MainActivity) : LifecycleHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        with(mainActivity) {
            bottom_navigator.setOnNavigationItemSelectedListener(this)
        }
    }
}


class MainActivity :
    BaseSwipeActivity<MainViewModel>(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    OnLessonClickListener {

    override val myViewModel: MainViewModel by viewModel()

    override val lifecycleHandlers: List<LifecycleHandler> = listOf(MainInitHandler(this))

    override val toolbarToUse: Toolbar get() = toolbar

    override val layoutRes: Int = R.layout.activity_main

    private val settings: Settings by inject()

    override val swipe = Swipe(40, 300)

    lateinit var activeFragment: Fragment

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            myViewModel.init()
        }

        loadFragment(get<HomeFragment> { parametersOf(this) })

        val service = NotificationService()
        val mIntent = Intent(this, service::class.java)
        if (!isMyServiceRunning(service::class.java)) {
            startService(mIntent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_search -> {
                if (activeFragment !is DictionaryFragment) {
                    loadFragment(get<DictionaryFragment> { parametersOf() })
                    return true
                }
            }
            R.id.nav_edit_lesson -> {
                if (activeFragment !is EditFragment) {
                    loadFragment(get<EditFragment> { parametersOf() })
                    return true
                }

            }

            R.id.nav_home -> {
                if (activeFragment !is HomeFragment) {
                    loadFragment(get<HomeFragment> { parametersOf(this) })
                    return true
                }
            }

        }
        return false
    }

    private fun swiped(right: Boolean) {
        with(
            with(getIdFromFragment(activeFragment)) {
                if (right)
                    if (this == 2) 0 else this + 1
                else
                    if (this == 0) 2 else this - 1
            }
        )
        {
            bottom_navigator.selectedItemId = bottom_navigator.menu.getItem(this).itemId
            loadFragment(getFragmentById(this))
        }

    }


    override fun onSwipeEventLeft(): Boolean {
        swiped(false)
        return true
    }


    override fun onSwipeEventRight(): Boolean {
        swiped(true)
        return true
    }

    private fun getFragmentById(newFragmentId: Int): Fragment {
        return when (newFragmentId) {
            0 -> get<HomeFragment> { parametersOf(this) }
            1 -> get<EditFragment>()
            else -> get<DictionaryFragment>()
        }
    }

    private fun getIdFromFragment(fragment: Fragment): Int {
        return when (fragment::class) {
            HomeFragment::class -> 0
            EditFragment::class -> 1
            else -> 2
        }
    }


    @ExperimentalCoroutinesApi
    override fun onPause() {
        super.onPause()
        runBlocking {
            val id = MainContext.EditContext.lesson.value?.id ?: return@runBlocking
            settings.activeLessonId = id.toInt()

        }
    }

    private fun loadFragment(fragment: Fragment) {
        activeFragment = fragment

        if (fragment is DictionaryFragment || fragment is EditFragment) {
            val oldMenuItem = toolbarToUse.menu.findFirst { each -> each.itemId == 1 }
            if (oldMenuItem == null) {
                toolbarToUse.menu.add(Menu.NONE, 1, Menu.NONE, "Add Vocable")
                val menuItem = toolbarToUse.menu.findItem(1)
                menuItem.setOnMenuItemClickListener {
                    val dialog: NewVocableDialog by inject { parametersOf(this) }
                    dialog.show(supportFragmentManager, "New Vocable")
                    true
                }
            }
            toolbarToUse.menu.removeItem(R.id.action_new_lesson)
        } else {
            toolbarToUse.menu.removeItem(1)
            toolbarToUse.menu.add(Menu.NONE, R.id.action_new_lesson, Menu.NONE, R.string.new_lesson)
        }

        val newSelectedItem = when (fragment) {
            is HomeFragment -> R.id.nav_home
            is DictionaryFragment -> R.id.nav_search
            else -> R.id.nav_edit_lesson
        }
        bottom_navigator.selectedItemId = newSelectedItem
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.action_new_lesson -> {
                val fragment =
                    NewLessonDialog(
                        de.hamurasa.data.lesson.Lesson(lastChanged = DateTime.now()),
                        activeFragment as HomeFragment
                    )
                fragment.show(supportFragmentManager, "New Lesson")
            }
            R.id.action_export -> {
                myViewModel.launchJob {
                    val json = myViewModel.export()
                    val clipboard =
                        getSystemService(ClipboardManager::class.java) as ClipboardManager

                    withContext(Dispatchers.Main) {
                        val dialog = ImportExportDialog(json, clipboard)
                        dialog.show(supportFragmentManager, "Import/Export Json")
                    }
                }
            }
            R.id.action_new_profile -> {
                val newProfileDialog: NewProfileDialog by inject()
                newProfileDialog.show(supportFragmentManager, "New Profile")
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.runningAppProcesses) {
            if (serviceClass.name == service.processName) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }

    override fun onLessonClick(lesson: de.hamurasa.data.lesson.Lesson) {
        bottom_navigator.selectedItemId = R.id.nav_edit_lesson
        loadFragment(get<EditFragment>())

    }

}
