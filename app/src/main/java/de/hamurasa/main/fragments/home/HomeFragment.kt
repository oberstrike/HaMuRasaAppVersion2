package de.hamurasa.main.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.mitteloupe.solid.fragment.handler.LifecycleHandler
import de.hamurasa.R
import de.hamurasa.data.lesson.Lesson
import de.hamurasa.data.profile.Profile
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.adapters.ILessonRecyclerViewListener
import de.hamurasa.main.fragments.adapters.LessonKotlinModel
import de.hamurasa.main.fragments.adapters.OnLessonClickListener
import de.hamurasa.session.SessionActivity
import de.hamurasa.session.models.SessionEvent
import de.hamurasa.session.models.VocableWrapper
import de.hamurasa.util.AbstractSelfCleaningFragment
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import okhttp3.internal.notify
import org.angmarch.views.NiceSpinner
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel


class ProfileHandler(
    private val homeViewModel: HomeViewModel
) : LifecycleHandler {

    private lateinit var profileSpinner: NiceSpinner

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val profiles = homeViewModel.getAllProfiles().map { it.name }
        profileSpinner = view.findViewById(R.id.profileSpinner)

        profileSpinner.apply {
            attachDataSource(profiles)

            setOnSpinnerItemSelectedListener { parent, _, position, _ ->
                val item = parent.getItemAtPosition(position) as String
                homeViewModel.launchJob {
                    val profile = homeViewModel.findProfileByName(item) ?: return@launchJob
                    MainContext.HomeContext.change(profile)
                }
            }
        }


    }


}


class HomeFragment(private val onItemClickListener: OnLessonClickListener) :
    AbstractSelfCleaningFragment(), ILessonRecyclerViewListener {

    override val layoutId: Int = R.layout.home_fragment

    override val myViewModel: HomeViewModel by sharedViewModel()

    override val lifecycleHandlers: List<LifecycleHandler> = listOf(
        ProfileHandler(
            get()
        )
    )

    private var position: Int = 0

    override fun onLessonLongClick(position: Int) {
        this.position = position
    }

    private val sessionEvent: SessionEvent by inject()

    private val controller = SampleKotlinController(this)


    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lessonsRecyclerView

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


    @ExperimentalCoroutinesApi
    override fun onLessonClick(lesson: Lesson) {
        myViewModel.launchJob {
            GlobalScope.launch(Dispatchers.IO) {
                MainContext.EditContext.change(lesson)

                withContext(Dispatchers.Main) {
                    onItemClickListener.onLessonClick(lesson)
                }

            }
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
                onActionDeleteExercise()
            }
            R.id.action_rename -> {
                onActionRenameExercise()
            }
            R.id.action_start -> {
                onActionStart()
            }
        }
        return super.onContextItemSelected(item)
    }

    @ExperimentalCoroutinesApi
    private fun onActionDeleteExercise() {
        val lesson = MainContext.HomeContext.value()?.lessons?.get(position) ?: return
        val deleteLessonDialog = DeleteLessonDialog(lesson)
        deleteLessonDialog.show(parentFragmentManager, "Delete")
    }

    private fun onActionRenameExercise() {
        /*'
    }
        val position = lessonRecyclerViewAdapter.position
        val lesson = lessonRecyclerViewAdapter.getLesson(position)
    */
    }

    @ExperimentalCoroutinesApi
    private fun onActionStart() {
        val lesson = MainContext.HomeContext.value()?.lessons?.get(position) ?: return

        if (lesson.words.isEmpty())
            return

        sessionEvent.vocables = lesson.words.map { VocableWrapper(it) }
        sessionEvent.activeVocable = sessionEvent.vocables.random()

        val intent = Intent(activity, SessionActivity::class.java)

        startActivity(intent)

    }

}


class SampleKotlinController(
    private val lessonRecyclerViewListener: ILessonRecyclerViewListener
) : TypedEpoxyController<List<Lesson>>() {

    override fun buildModels(data1: List<Lesson>) {
        data1.forEachIndexed { index, lesson ->
            LessonKotlinModel(lesson, lessonRecyclerViewListener, index)
                .id("$index")
                .addTo(this)

        }
    }


}