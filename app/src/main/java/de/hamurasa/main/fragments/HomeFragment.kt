package de.hamurasa.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.MainViewModel
import de.hamurasa.main.fragments.adapters.LessonRecyclerViewAdapter
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment(), LessonRecyclerViewAdapter.OnClickListener {

    private val myViewModel: MainViewModel by sharedViewModel()

    private lateinit var recyclerView: RecyclerView

    private lateinit var lessonRecyclerViewAdapter: LessonRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, main_container, false)
    }

    override fun onStart() {
        super.onStart()
        recyclerView = view!!.findViewById(R.id.lessonsRecyclerView)
        init()
    }


    private fun init() {
        lessonRecyclerViewAdapter =
            LessonRecyclerViewAdapter(
                context!!,
                this
            )
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.adapter = lessonRecyclerViewAdapter

        myViewModel.launch {
            myViewModel.lessons
                .subscribeOn(myViewModel.provider.computation())
                .observeOn(myViewModel.provider.ui())
                .subscribe {
                    lessonRecyclerViewAdapter.setLessons(it)
                    lessonRecyclerViewAdapter.notifyDataSetChanged()
                }
        }
        myViewModel.update()
    }


    /*
    override fun onItemClick(position: Int) {
        val lesson = myViewModel.lessons.blockingFirst()[position]
        val words = lesson.words
        SessionContext.vocables = words
        if (lesson.words.size == 0)
            return

        val intent = Intent(activity, SessionActivity::class.java)
        startActivity(intent)
    }*/


    override fun onItemClick(position: Int) {
        val lesson = myViewModel.lessons.blockingFirst()[position]
        val vocables = lesson.words
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
        }
        return super.onContextItemSelected(item)
    }

    private fun onActionDeleteExercise() {
        val position = lessonRecyclerViewAdapter.position
        val lesson = lessonRecyclerViewAdapter.getLesson(position)
        myViewModel.deleteLesson(lesson)
        println(lesson)
    }

    private fun onActionRenameExercise() {

    }


}