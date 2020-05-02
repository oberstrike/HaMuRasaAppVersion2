package de.hamurasa.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.MainViewModel
import de.hamurasa.main.fragments.adapters.LessonRecyclerViewAdapter
import de.hamurasa.session.SessionActivity
import de.hamurasa.session.SessionContext
import de.hamurasa.session.models.VocableWrapper
import de.util.hamurasa.utility.AbstractFragment
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.home_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HomeFragment : AbstractFragment(), LessonRecyclerViewAdapter.OnClickListener {
    private val myViewModel: MainViewModel by sharedViewModel()


    private lateinit var lessonRecyclerViewAdapter: LessonRecyclerViewAdapter

    override fun getLayoutId(): Int = R.layout.home_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myViewModel.updateHome()
        initElements()
    }

    private fun initElements() {
        lessonRecyclerViewAdapter = LessonRecyclerViewAdapter(context!!, this)
        lessonsRecyclerView.layoutManager = LinearLayoutManager(context!!)
        lessonsRecyclerView.adapter = lessonRecyclerViewAdapter

        myViewModel.observe(MainContext.HomeContext.lessons) {
            lessonRecyclerViewAdapter.setLessons(it)
            lessonRecyclerViewAdapter.notifyDataSetChanged()
        }

    }


    override fun onItemClick(position: Int) {
        val lesson = MainContext.HomeContext.lessons.blockingFirst()[position]
        MainContext.EditContext.lesson = BehaviorSubject.create()
        MainContext.EditContext.lesson.onNext(lesson)
    }


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

    private fun onActionDeleteExercise() {
        val position = lessonRecyclerViewAdapter.position
        val lesson = lessonRecyclerViewAdapter.getLesson(position)

        myViewModel.deleteLesson(lesson)
        val value = MainContext.EditContext.lesson.value
        if (value != null) {
            if (value.id == lesson.id) {
                MainContext.EditContext.lesson.onComplete()
            }

        }

    }

    private fun onActionRenameExercise() {
        val position = lessonRecyclerViewAdapter.position
        val lesson = lessonRecyclerViewAdapter.getLesson(position)


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

    override fun onStop() {
        myViewModel.onCleared()
        super.onStop()
    }


}