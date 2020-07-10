package de.hamurasa.main.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.adapters.LessonRecyclerViewAdapter
import de.hamurasa.session.SessionActivity
import de.hamurasa.session.SessionContext
import de.hamurasa.session.models.VocableWrapper
import de.util.hamurasa.utility.main.AbstractViewModel
import de.util.hamurasa.utility.util.AbstractFragment
import de.util.hamurasa.utility.util.dialog
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HomeFragment(private val onItemClickListener: OnItemClickListener) : AbstractFragment(),
    LessonRecyclerViewAdapter.OnClickListener {

    override val myViewModel: HomeViewModel by sharedViewModel()

    private lateinit var lessonRecyclerViewAdapter: LessonRecyclerViewAdapter

    override fun getLayoutId(): Int = R.layout.home_fragment

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Init Observer
        myViewModel.launchJob {
            myViewModel.updateHome()
            initObserver()
        }
        //Init Views
        initElements()
    }

    private fun initElements() {
        lessonRecyclerViewAdapter = LessonRecyclerViewAdapter(requireContext(), this)
        lessonsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        lessonsRecyclerView.adapter = lessonRecyclerViewAdapter
    }

    @ExperimentalCoroutinesApi
    private suspend fun initObserver() {
        MainContext.HomeContext.lessons.collect {
            if (it == null)
                return@collect
            withContext(Dispatchers.Main) {
                lessonRecyclerViewAdapter.setLessons(it)
                lessonRecyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }


    //ONCLICK LESSON
    @ExperimentalCoroutinesApi
    override fun onItemClick(position: Int) {
        myViewModel.launchJob {
            GlobalScope.launch(Dispatchers.IO) {
                val lessons = MainContext.HomeContext.lessons.value ?: return@launch
                val lesson = lessons[position]
                MainContext.EditContext.setLesson(lesson)

                withContext(Dispatchers.Main) {
                    onItemClickListener.onItemClick()
                }

            }
        }

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
        val position = lessonRecyclerViewAdapter.position
        val lesson = lessonRecyclerViewAdapter.getLesson(position)

        dialog(R.layout.dialog_delete_lesson) { dialog ->
            val deleteLessonButton =
                dialog.requireView().findViewById<Button>(R.id.delete_lesson_button)

            deleteLessonButton.setOnClickListener {
                myViewModel.deleteLesson(lesson)
                dialog.dismiss()
            }
        }.show(parentFragmentManager, "Delete")


    }

    private fun onActionRenameExercise() {
        val position = lessonRecyclerViewAdapter.position
        val lesson = lessonRecyclerViewAdapter.getLesson(position)
        //TODO

    }

    private fun onActionStart() {
        val position = lessonRecyclerViewAdapter.position
        val lesson = lessonRecyclerViewAdapter.getLesson(position)

        if (lesson.words.isEmpty())
            return

        SessionContext.vocables = lesson.words.map { VocableWrapper(it) }

        val intent = Intent(activity, SessionActivity::class.java)
        startActivity(intent)
    }
}

interface OnItemClickListener {
    fun onItemClick()
}