package de.hamurasa.util

import com.github.pwittchen.swipe.library.rx2.Swipe
import com.github.pwittchen.swipe.library.rx2.SwipeEvent
import de.util.hamurasa.utility.util.AbstractActivity

abstract class AbstractSwipeActivity<T : BaseViewModel> : AbstractActivity<T>() {

    abstract val swipe: Swipe

    abstract fun onSwipeEventLeft(): Boolean

    abstract fun onSwipeEventRight(): Boolean

    override fun init() {
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

}