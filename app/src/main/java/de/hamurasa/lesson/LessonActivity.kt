package de.hamurasa.lesson

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.github.pwittchen.swipe.library.rx2.Swipe
import com.github.pwittchen.swipe.library.rx2.SwipeEvent
import de.hamurasa.R
import de.hamurasa.main.MainActivity
import de.hamurasa.lesson.fragments.VocableFragment

import kotlinx.android.synthetic.main.activity_lesson.*
import org.koin.android.viewmodel.ext.android.viewModel

class LessonActivity : AppCompatActivity() {

    private val myViewModel: LessonViewModel by viewModel()

    private lateinit var swipe: Swipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        setSupportActionBar(toolbar)
        swipe = Swipe(40, 200)


        myViewModel.launch {
            swipe.observe().subscribeOn(myViewModel.provider.computation())
                .observeOn(myViewModel.provider.ui())
                .subscribe {
                    if (it == SwipeEvent.SWIPED_RIGHT) {
                        supportFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.vocable_container, VocableFragment())
                            .commit()
                        myViewModel.next(true)
                    } else if (it == SwipeEvent.SWIPED_LEFT) {
                        supportFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.vocable_container, VocableFragment())
                            .commit()
                        myViewModel.next(false)
                    }
                }
        }
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_login, menu)

        return super.onCreateOptionsMenu(menu)
    }


    private fun init() {
        myViewModel.init()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.vocable_container, VocableFragment())
            .commit()

        myViewModel.launch {
            myViewModel.isRunning
                .subscribeOn(myViewModel.provider.computation())
                .observeOn(myViewModel.provider.ui())
                .subscribe {
                    if(!it){
                        myViewModel.stop()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    }
                }

        }
    }


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        swipe.dispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }
}
