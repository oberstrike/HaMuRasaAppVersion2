package de.hamurasa.main.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.*
import de.hamurasa.R
import de.hamurasa.data.lesson.Lesson
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.adapters.*
import de.hamurasa.data.profile.Profile
import de.hamurasa.session.SessionActivity
import de.hamurasa.session.models.SessionEvent
import de.hamurasa.session.models.VocableWrapper
import de.hamurasa.util.AbstractSelfCleanupFragment
import de.hamurasa.util.epoxy.KotlinModel
import de.hamurasa.util.widgets.bind
import de.hamurasa.util.widgets.initAdapter
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel


class HomeFragment(private val onItemClickListener: OnLessonClickListener) :
    AbstractSelfCleanupFragment(), ILessonRecyclerViewListener {

    private var position: Int = 0

    override fun onLessonLongClick(position: Int) {
        this.position = position
    }

    override val myViewModel: HomeViewModel by sharedViewModel()

    private val sessionEvent: SessionEvent by inject()

    //  private lateinit var lessonRecyclerLessonViewAdapter: SolidAdapter<SolidLessonViewHolder, Lesson>

    // private lateinit var lessonRecyclerLessonViewAdapter: Typed2EpoxyController<List<Lesson>, Boolean>

    private val controller = SampleKotlinController()

    @ExperimentalCoroutinesApi
    private var activeProfile: Profile = MainContext.HomeContext.value()!!

    override fun getLayoutId(): Int = R.layout.home_fragment

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profiles = myViewModel.getAllProfiles().map { it.name }.toTypedArray()
        profileSpinner.initAdapter(profiles)

        myViewModel.launchJob {
            profileSpinner.bind(this::activeProfile,
                toString = {
                    it.name
                }, converter = {
                    runBlocking {
                        myViewModel.findProfileByName(it)!!
                    }
                }, onValueChanged = {
                    launchJob {
                        MainContext.HomeContext.change(it)
                    }
                })
        }

        //Init Views
        initElements()

        launchJob {
            myViewModel.updateHome()
            initObserver()
        }


    }


    private fun initElements() {
        /*
        lessonRecyclerLessonViewAdapter = SolidAdapter(
            viewBinder = SolidLessonViewBinder(this),
            viewProvider = SolidViewProvider(layoutInflater),
            viewHoldersProvider = { view, _ -> SolidLessonViewHolder(view) }
        )*/

        lessonsRecyclerView.adapter = controller.adapter
    }

    @ExperimentalCoroutinesApi
    private suspend fun initObserver() {
        MainContext.HomeContext.flow.collect {
            if (it == null)
                return@collect
            withContext(Dispatchers.Main) {
                updateRecyclerView(it)
            }
        }
    }

    fun updateRecyclerView(it: Profile) {
        try {
            controller.setData(it.lessons, true)
            lessonsRecyclerView.requestModelBuild()
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


class SampleKotlinController : Typed2EpoxyController<List<Lesson>, Boolean>() {

    override fun buildModels(data1: List<Lesson>, data2: Boolean?) {
        data1.forEachIndexed { index, lesson ->
            LessonKotlinModel("lesson ${lesson.id}")
                .id("data class $index")
                .addTo(this)
        }
    }


}