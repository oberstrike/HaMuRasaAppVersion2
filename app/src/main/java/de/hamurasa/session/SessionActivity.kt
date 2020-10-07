package de.hamurasa.session

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import com.github.pwittchen.swipe.library.rx2.Swipe
import com.mitteloupe.solid.activity.handler.LifecycleHandler
import de.hamurasa.R
import de.hamurasa.main.MainActivity
import de.hamurasa.session.fragments.*
import de.hamurasa.session.models.SessionEvent
import de.hamurasa.session.models.SessionType
import de.hamurasa.util.BaseSwipeActivity
import de.hamurasa.util.toast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.concurrent.fixedRateTimer

class TimerHandler(private val session: SessionEvent) : LifecycleHandler, KoinComponent {
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timer = fixedRateTimer(period = 1000L, daemon = true)
        {
            session.time += 1
            println(session.time)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}


class SessionActivity : BaseSwipeActivity<SessionViewModel>() {

    override val lifecycleHandlers: List<LifecycleHandler> = listOf(get<TimerHandler>())

    override val myViewModel: SessionViewModel by viewModel()

    override val layoutRes = R.layout.activity_lesson

    override val toolbarToUse: Toolbar? = null

    override val swipe = Swipe(40, 300)

    private var oldFragment: IBasicFragment? = null

    val session: SessionEvent by inject()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myViewModel.init()
        val fragment = getNewFragment()
        changeFragment(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            fragment
        )

    }

    @ExperimentalCoroutinesApi
    override fun onSwipeEventLeft(): Boolean {
        return onSwipe(false)
    }

    @ExperimentalCoroutinesApi
    override fun onSwipeEventRight(): Boolean {
        return onSwipe(true)
    }

    @ExperimentalCoroutinesApi
    private fun onSwipe(right: Boolean): Boolean {
        val isRunning = myViewModel.next(true)

        if (!isRunning) {
            close()
            return true
        }
        if (right) changeFragment(R.anim.enter_from_left, R.anim.exit_to_right, getNewFragment())
        else changeFragment(R.anim.enter_from_right, R.anim.exit_to_left, getNewFragment())
        return true
    }

    override fun onDestroy() {
        myViewModel.onCleared()
        super.onDestroy()
    }


    @ExperimentalCoroutinesApi
    fun getNewFragment(): BasicFragment {
        return when (session.sessionType) {
            SessionType.STANDARD -> get<StandardFragmentAbstract> { parametersOf(session.activeVocable) }
            SessionType.ALTERNATIVE -> get<AlternativeFragmentAbstract> { parametersOf(session.activeVocable) }
            else -> get<WritingFragment> {
                parametersOf(
                    session.activeVocable,
                    this::onTextChange
                )
            }
        }

    }

    fun changeFragment(leftLayout: Int, rightLayout: Int, fragment: BasicFragment) {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun close() {
        val newIntent = Intent(this, MainActivity::class.java)
        startActivity(newIntent)


        toast("You have successfully completed the training!")
        finish()
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
