package de.hamurasa.session

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.github.pwittchen.swipe.library.rx2.Swipe
import com.github.pwittchen.swipe.library.rx2.SwipeEvent
import de.hamurasa.R
import de.hamurasa.main.MainActivity
import de.hamurasa.session.fragments.VocableFragment
import de.hamurasa.session.models.VocableWrapper
import de.util.hamurasa.utility.toast

import kotlinx.android.synthetic.main.activity_lesson.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SessionActivity : AppCompatActivity() {

    private val myViewModel: SessionViewModel by viewModel()

    private lateinit var swipe: Swipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        setSupportActionBar(toolbar)
        swipe = Swipe(40, 200)

        myViewModel.observe(swipe.observe()) {
            if (it == SwipeEvent.SWIPED_RIGHT) {
                changeFragment(R.anim.enter_from_left, R.anim.exit_to_right)
                myViewModel.next(true)
            } else if (it == SwipeEvent.SWIPED_LEFT) {
                changeFragment(R.anim.enter_from_right, R.anim.exit_to_left)
                myViewModel.next(false)
            }
        }

        myViewModel.init()
        init()
    }

    private fun changeFragment(leftLayout: Int, rightLayout: Int) {
        val fragment by inject<VocableFragment>()
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(leftLayout, rightLayout)
            .replace(R.id.vocable_container, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_login, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun init() {
        val fragment by inject<VocableFragment>()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.vocable_container, fragment)
            .commit()

        myViewModel.observe(SessionContext.running){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            toast("You have successfully completed the training!")
            finish()

        }

    }


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        swipe.dispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }
}
