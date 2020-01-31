package de.hamurasa.main

import android.content.AbstractThreadedSyncAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import de.hamurasa.R
import de.hamurasa.login.LoginActivity
import de.hamurasa.main.fragments.LessonRecylerViewAdapter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val myViewModel: MainViewModel by viewModel()

    private lateinit var lessonRecyclerViewAdapter: LessonRecylerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        myViewModel.launch {
            myViewModel.isLoggedIn
                .takeLast(1)
                .subscribeOn(myViewModel.provider.computation())
                .observeOn(myViewModel.provider.ui())
                .subscribe {
                    if (!it) {
                        doAnimation()
                    }else{
                        init()
                    }
                }
        }

        myViewModel.init()
    }


    private fun doAnimation() {
        val appear = hello_textView.animate()
        appear.alpha(0.8f)
        appear.withEndAction {
            val disappear = hello_textView.animate()
            disappear.alpha(0f)
            disappear.duration = 2000
            disappear.withEndAction {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            disappear.start()
        }.setDuration(2000).start()
    }

    private fun init(){
        lessonRecyclerViewAdapter = LessonRecylerViewAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
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
}
