package de.hamurasa.session

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.pwittchen.swipe.library.rx2.Swipe
import com.github.pwittchen.swipe.library.rx2.SwipeEvent
import de.hamurasa.R
import de.hamurasa.main.MainActivity
import de.hamurasa.session.models.VocableWrapper
import de.hamurasa.settings.SettingsContext
import de.util.hamurasa.utility.AbstractFragment
import de.util.hamurasa.utility.afterTextChanged
import de.util.hamurasa.utility.bind
import de.util.hamurasa.utility.toast
import kotlinx.android.synthetic.main.activity_lesson.*
import kotlinx.android.synthetic.main.vocable_session_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.reflect.KMutableProperty0

class SessionActivity : AppCompatActivity() {

    private val myViewModel: SessionViewModel by viewModel()

    private lateinit var swipe: Swipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        setSupportActionBar(toolbar)
        swipe = Swipe(40, 200)

        myViewModel.observe(swipe.observe()) {
            if (SessionContext.sessionType != SessionType.WRITING) {
                if (it == SwipeEvent.SWIPED_RIGHT) {
                    myViewModel.next(true) ?: return@observe
                    changeFragment(R.anim.enter_from_left, R.anim.exit_to_right)
                } else if (it == SwipeEvent.SWIPED_LEFT) {
                    myViewModel.next(false) ?: return@observe
                    changeFragment(R.anim.enter_from_right, R.anim.exit_to_left)
                }
            }
        }
        myViewModel.init()
        init()
    }


    private fun changeFragment(leftLayout: Int, rightLayout: Int) {
        val fragment = BasicFragment(this::onTextChange)


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
        changeFragment(R.anim.enter_from_left, R.anim.exit_to_right)


        myViewModel.observe(SessionContext.running) {
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


    class BasicFragment(private val onTextChange: (String) -> Unit) : AbstractFragment(),
        View.OnClickListener {

        override fun getLayoutId() = R.layout.vocable_session_fragment

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            session_vocable_second_value.setOnClickListener(this)

            with(SessionContext.activeVocable) {
                val value = when (SessionContext.sessionType) {
                    SessionType.STANDARD -> this.value
                    SessionType.ALTERNATIVE -> this.translation
                    else -> this.translation
                }

                if (SessionContext.sessionType == SessionType.WRITING) {
                    session_value_editText.afterTextChanged(onTextChange)
                } else {
                    session_value_editText.visibility = View.INVISIBLE
                }

                session_vocable_first_value.text = value
                vocableProgressBar.parent
                vocableProgressBar.progress =
                    (this.level - 1) * (100 / SettingsContext.SessionSettings.maxRepetitions)
            }
        }

        override fun onClick(v: View?) {
            with(SessionContext.activeVocable) {
                val value = when (SessionContext.sessionType) {
                    SessionType.STANDARD -> this.translation
                    SessionType.ALTERNATIVE -> this.value
                    else -> this.value
                }

                session_vocable_second_value.text = value


            }
        }

    }

    private fun onClick() {

    }

    private fun onTextChange(value: String) {
        if (value.isNotEmpty()) {
            if (value == SessionContext.activeVocable.value) {
                myViewModel.next(true) ?: return
                changeFragment(R.anim.enter_from_left, R.anim.exit_to_right)
            }
        }
    }

}
