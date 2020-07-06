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
import de.util.hamurasa.utility.util.AbstractFragment
import de.util.hamurasa.utility.util.dialog
import de.util.hamurasa.utility.util.requestAsync
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.home_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HomeFragment(private val onItemClickListener: OnItemClickListener) : AbstractFragment(),
    LessonRecyclerViewAdapter.OnClickListener {

    private val myViewModel: HomeViewModel by sharedViewModel()

    private lateinit var lessonRecyclerViewAdapter: LessonRecyclerViewAdapter

    override fun getLayoutId(): Int = R.layout.home_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myViewModel.updateHome()
        requestAsync {
            initElements()
        }
    }

    private fun initElements() {
        lessonRecyclerViewAdapter = LessonRecyclerViewAdapter(requireContext(), this)
        lessonsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
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
        onItemClickListener.onItemClick()
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

        dialog(R.layout.dialog_delete_lesson) { dialog ->
            val deleteLessonButton =
                dialog.requireView().findViewById<Button>(R.id.delete_lesson_button)

            deleteLessonButton.setOnClickListener {
                myViewModel.deleteLesson(lesson)
                val value = MainContext.EditContext.lesson.value
                if (value != null) {
                    if (value.id == lesson.id) {
                        MainContext.EditContext.lesson.onComplete()
                    }
                }
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

    override fun onStop() {
        myViewModel.onCleared()
        super.onStop()
    }
}

interface OnItemClickListener {
    fun onItemClick()
}