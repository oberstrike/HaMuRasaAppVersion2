package de.hamurasa.session

import android.content.Intent
import android.view.Menu
import android.view.MotionEvent
import androidx.appcompat.widget.Toolbar
import com.github.pwittchen.swipe.library.rx2.Swipe
import de.hamurasa.R
import de.hamurasa.main.MainActivity
import de.hamurasa.session.fragments.AlternativeFragment
import de.hamurasa.session.fragments.BasicFragment
import de.hamurasa.session.fragments.StandardFragment
import de.hamurasa.session.fragments.WritingFragment
import de.hamurasa.session.models.SessionEvent
import de.hamurasa.session.models.SessionType
import de.hamurasa.util.AbstractSwipeActivity
import de.util.hamurasa.utility.util.toast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.concurrent.fixedRateTimer

class SessionActivity : AbstractSwipeActivity<SessionViewModel>() {

    override val myViewModel: SessionViewModel by viewModel()

    override val layoutRes = R.layout.activity_lesson

    override val toolbarToUse: Toolbar? = null

    override val swipe = Swipe(40, 300)

    private var oldFragment: BasicFragment? = null

    lateinit var timer: Timer

    private val session: SessionEvent by inject()

    @ExperimentalCoroutinesApi
    override fun onSwipeEventLeft(): Boolean {
        val isRunning = myViewModel.next(false)
        if (!isRunning) {
            close()
            return true
        }
        changeFragment(R.anim.enter_from_right, R.anim.exit_to_left, getNewFragment())
        return true
    }

    @ExperimentalCoroutinesApi
    override fun onSwipeEventRight(): Boolean {
        val isRunning = myViewModel.next(true)
        if (!isRunning) {
            close()
            return true
        }

        changeFragment(R.anim.enter_from_left, R.anim.exit_to_right, getNewFragment())
        return true
    }

    override fun onDestroy() {
        myViewModel.onCleared()
        timer.cancel()
        super.onDestroy()
    }


    @ExperimentalCoroutinesApi
    override fun init() {
        super.init()
        myViewModel.init()

        timer = fixedRateTimer(period = 1000L, daemon = true)
        {
            session.time += 1
            println(session.time)
        }


        val fragment = getNewFragment()
        changeFragment(R.anim.enter_from_right, R.anim.exit_to_left, fragment)
    }

    @ExperimentalCoroutinesApi
    private fun getNewFragment(): BasicFragment {
        return when (session.sessionType) {
            SessionType.STANDARD -> get<StandardFragment> { parametersOf(session.activeVocable) }
            SessionType.ALTERNATIVE -> get<AlternativeFragment> { parametersOf(session.activeVocable) }
            else -> get<WritingFragment> {
                parametersOf(
                    session.activeVocable,
                    this::onTextChange
                )
            }
        }

    }

    private fun changeFragment(leftLayout: Int, rightLayout: Int, fragment: BasicFragment) {
        oldFragment = if (oldFragment == null)
            fragment
        else {
            oldFragment!!.reset()
            fragment
        }

        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(leftLayout, rightLayout)
            .replace(R.id.vocable_container, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun close() {
        val intent2 = Intent(this, MainActivity::class.java)
        startActivity(intent2)
        toast("You have successfully completed the training!")
        finish()
    }


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        swipe.dispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }


    @ExperimentalCoroutinesApi
    private fun onTextChange(value: String) {
        if (value.isNotEmpty()) {
            if (value == session.activeVocable.value) {
                if ((oldFragment!! as WritingFragment).isPressed)
                    onSwipeEventLeft()
                else
                    onSwipeEventRight()
            }
        }
    }

}
