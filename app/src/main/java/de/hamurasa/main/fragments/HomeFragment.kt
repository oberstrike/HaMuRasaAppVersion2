package de.hamurasa.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hamurasa.R
import de.hamurasa.lesson.LessonActivity
import de.hamurasa.lesson.LessonContext
import de.hamurasa.lesson.LessonViewModel
import de.hamurasa.login.LoginActivity
import de.hamurasa.main.MainViewModel
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.home_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment(), LessonRecylerViewAdapter.OnClickListener {

    private val myViewModel: MainViewModel by sharedViewModel()
    private lateinit var recyclerView: RecyclerView


    private lateinit var lessonRecyclerViewAdapter: LessonRecylerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, main_container, false)
    }

    override fun onStart() {
        recyclerView = view!!.findViewById(R.id.recyclerView)
        checkLoggedIn()
        super.onStart()
    }


    private fun doAnimation() {
        val appear = hello_textView.animate()
        appear.alpha(0.8f)
        appear.withEndAction {
            val disappear = hello_textView.animate()
            disappear.alpha(0f)
            disappear.duration = 2000
            disappear.withEndAction {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity!!.finish()
            }
            disappear.start()
        }.setDuration(2000).start()
    }

    private fun init() {
        lessonRecyclerViewAdapter = LessonRecylerViewAdapter(context!!, this)
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

    private fun checkLoggedIn() {
        myViewModel.launch {
            myViewModel.isLoggedIn
                .takeLast(1)
                .subscribeOn(myViewModel.provider.computation())
                .observeOn(myViewModel.provider.ui())
                .subscribe {
                    if (!it) {
                        doAnimation()
                    } else {
                        init()
                    }
                }
        }
    }

    override fun onItemClick(position: Int) {
        val lesson = myViewModel.lessons.blockingFirst()[position]
        val words = lesson.words
        LessonContext.vocables = words
        if (lesson.words.size == 0)
            return

        val intent = Intent(activity, LessonActivity::class.java)
        startActivity(intent)
    }


}