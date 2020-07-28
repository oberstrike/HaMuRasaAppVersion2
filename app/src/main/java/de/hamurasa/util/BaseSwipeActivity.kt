package de.hamurasa.util

import android.os.Bundle
import android.view.MotionEvent
import com.github.pwittchen.swipe.library.rx2.Swipe
import com.github.pwittchen.swipe.library.rx2.SwipeEvent

abstract class BaseSwipeActivity<T : BaseViewModel>(
) :
    BaseActivity<T>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myViewModel.observe(swipe.observe()) {
            when (it) {
                SwipeEvent.SWIPED_RIGHT -> {
                    onSwipeEventRight()
                }
                SwipeEvent.SWIPED_LEFT -> {
                    onSwipeEventLeft()
                }
                else -> {

                }
            }
        }
    }

    abstract val swipe: Swipe

    abstract fun onSwipeEventLeft(): Boolean

    abstract fun onSwipeEventRight(): Boolean


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        swipe.dispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }


}