package de.hamurasa.main.fragments.home

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.mitteloupe.solid.fragment.handler.LifecycleHandler
import de.hamurasa.R
import de.hamurasa.data.profile.Profile
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.home.lesson.*
import de.hamurasa.main.fragments.home.profile.ProfileHandler
import de.hamurasa.util.AbstractSelfCleaningFragment
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.sharedViewModel


class HomeFragment :
    AbstractSelfCleaningFragment() {

    override val layoutId: Int = R.layout.home_fragment

    override val myViewModel: HomeViewModel by sharedViewModel()

    override val lifecycleHandlers: List<LifecycleHandler> = listOf(
        ProfileHandler(
            get()
        )
    )

    private val onSessionStartListeners: List<IOnSessionStartListener> = listOf(
        OnSessionStartListener(
            get()
        )
    )
    private val onLessonLongClickListener: IOnLessonLongClickListener =
        OnLessonLongClickListenerImpl()

    private val onLessonClickListener: IOnLessonClickListener = OnLessonClickListenerImpl(get())

    private val onLessonDeleteListener: IOnLessonDeleteListener = OnLessonDeleteListenerImpl()

    private val controller =
        LessonRecyclerViewController(onLessonClickListener, onLessonLongClickListener, this)


    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        launchJob {
            initObserver()
        }

    }


    @ExperimentalCoroutinesApi
    private suspend fun initObserver() {
        MainContext.HomeContext.flow.collect {
            val profile = it.value ?: return@collect
            withContext(Dispatchers.Main) {
                updateRecyclerView(profile)
            }
        }
    }

    private fun updateRecyclerView(it: Profile) {
        try {
            lessonsRecyclerView.adapter = controller.adapter
            controller.setData(it.lessons)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }


    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {

        menu.add(Menu.NONE, R.id.action_delete, Menu.NONE, R.string.action_delete)
        menu.add(Menu.NONE, R.id.action_rename, Menu.NONE, R.string.action_rename)
        menu.add(Menu.NONE, R.id.action_start, Menu.NONE, R.string.start)
    }


    @ExperimentalCoroutinesApi
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //Wenn "Löschen" ausgewählt ist
            R.id.action_delete -> {
                onLessonDeleteListener.onDelete(
                    MainContext.HomeContext.value()?.lessons?.get(onLessonLongClickListener.getPosition())!!,
                    parentFragmentManager
                )
            }
            R.id.action_rename -> {
                onActionRenameExercise()
            }
            R.id.action_start -> {
                onSessionStartListeners.forEach {
                    it.onLessonStarted(
                        MainContext.HomeContext.value()?.lessons?.get(onLessonLongClickListener.getPosition())!!,
                        this.requireActivity()
                    )
                }
            }
        }
        return super.onContextItemSelected(item)
    }



    private fun onActionRenameExercise() {
        /*'
    }
        val position = lessonRecyclerViewAdapter.position
        val lesson = lessonRecyclerViewAdapter.getLesson(position)
    */
    }


}


