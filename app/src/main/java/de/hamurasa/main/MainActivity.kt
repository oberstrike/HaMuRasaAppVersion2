package de.hamurasa.main

import android.content.ClipboardManager
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.github.pwittchen.swipe.library.rx2.Swipe
import com.github.pwittchen.swipe.library.rx2.SwipeEvent
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hamurasa.R
import de.hamurasa.main.fragments.dialogs.ImportExportDialog
import de.hamurasa.main.fragments.dictionary.DictionaryFragment
import de.hamurasa.main.fragments.edit.EditFragment
import de.hamurasa.main.fragments.edit.NewVocableDialog
import de.hamurasa.main.fragments.home.HomeFragment
import de.hamurasa.main.fragments.home.NewLessonDialog
import de.hamurasa.main.fragments.home.OnItemClickListener
import de.hamurasa.model.lesson.Lesson
import de.hamurasa.settings.SettingsActivity
import de.hamurasa.settings.SettingsContext
import de.hamurasa.settings.model.Settings
import de.util.hamurasa.utility.util.AbstractActivity
import de.util.hamurasa.utility.util.findFirst
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


//Logger

class MainActivity : AbstractActivity<MainViewModel>(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    OnItemClickListener {

    override val myViewModel: MainViewModel by viewModel()

    override val toolbarToUse: Toolbar get() = toolbar

    override val layoutRes: Int = R.layout.activity_main

    private val settings: Settings by inject()

    private lateinit var swipe: Swipe

    override fun init() {
        bottom_navigator.setOnNavigationItemSelectedListener(this)
        SettingsContext.init(settings)

        loadFragment(get<HomeFragment> { parametersOf(this) })

        runBlocking {
            initSwipe()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_search -> {
                if (MainContext.activeFragment !is DictionaryFragment) {
                    loadFragment(get<DictionaryFragment> { parametersOf() })
                    return true
                }
            }
            R.id.nav_edit_lesson -> {
                if (MainContext.activeFragment !is EditFragment) {
                    loadFragment(get<EditFragment> { parametersOf() })
                    return true
                }

            }

            R.id.nav_home -> {
                if (MainContext.activeFragment !is HomeFragment) {
                    loadFragment(get<HomeFragment> { parametersOf(this) })
                    return true
                }
            }

        }
        return false
    }


    private fun initSwipe() {
        swipe = Swipe(60, 300)
        //Swipe Binding extreme short
        myViewModel.observe(swipe.observe()) {
            if (it != SwipeEvent.SWIPED_RIGHT && it != SwipeEvent.SWIPED_LEFT)
                return@observe

            val newFragmentId = with(MainContext.activeFragment) {
                with(getIdFromFragment(this)) {
                    when (it) {
                        SwipeEvent.SWIPED_RIGHT -> if (this == 2) 0 else this + 1
                        else -> if (this == 0) 2 else this - 1
                    }
                }
            }

            with(newFragmentId) {
                bottom_navigator.selectedItemId = bottom_navigator.menu.getItem(this).itemId
                loadFragment(getFragmentById(this))
            }

        }
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
            SettingsContext.activeLessonId = id.toInt()

        }
    }

    //Select Menu Item OnClick
    override fun onItemClick() {
        bottom_navigator.selectedItemId = R.id.nav_edit_lesson
        loadFragment(get<EditFragment>())
    }

    private fun loadFragment(fragment: Fragment) {
        MainContext.activeFragment = fragment

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
            if (fragment is DictionaryFragment) {
                modeSpinner.visibility = View.VISIBLE
            } else {
                modeSpinner.visibility = View.INVISIBLE
            }
        } else {
            toolbarToUse.menu.removeItem(1)
            modeSpinner.visibility = View.INVISIBLE
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

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        swipe.dispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null)
            return false

        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.action_new_lesson -> {
                val fragment =
                    NewLessonDialog(
                        Lesson(lastChanged = DateTime.now())
                    )
                fragment.show(supportFragmentManager, "New Lesson")
            }
            R.id.action_export -> {
                val json = myViewModel.export()
                val clipboard = getSystemService(ClipboardManager::class.java) as ClipboardManager

                val dialog = ImportExportDialog(json, clipboard)

                dialog.show(supportFragmentManager, "Import/Export Json")
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
