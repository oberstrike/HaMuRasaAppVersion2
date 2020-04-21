package de.hamurasa.lesson.session

import android.content.Context
import de.hamurasa.util.AbstractViewModel
import de.hamurasa.util.SchedulerProvider
import de.hamurasa.lesson.model.vocable.Vocable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class SessionViewModel(
    val provider: SchedulerProvider,
    val context: Context
) : AbstractViewModel() {
    lateinit var activeVocable: Observable<Vocable>

    private var stats: HashMap<Long, Int> = HashMap()

    var position: Int = 0

    lateinit var isRunning: BehaviorSubject<Boolean>


    fun init() {
        if (SessionContext.vocables != null) {
            activeVocable = Observable.just(SessionContext.vocables!![position])
        }

        isRunning = BehaviorSubject.create()
        isRunning.onNext(true)
    }


    fun next(correct: Boolean) {
        val vocable = activeVocable.blockingFirst()
        val contains = stats.containsKey(vocable.id)
        val count = if (!contains) {
            if (correct) 1 else 0
        } else {
            val old = stats[vocable.id]!!
            if (correct) old + 1 else old - 1
        }

        stats[vocable.id] = count
        stats.forEach {
            println(it)
        }

        if (SessionContext.vocables!!.size != stats.size) {
            changeVocable()
        } else {
            if (stats.filter { it.value < 3 }.isEmpty()) {
                isRunning.onNext(false)
            } else {
                changeVocable()
            }

        }
    }


    private fun changeVocable() {
        val active = activeVocable.blockingFirst()
        var vocable: Vocable? = null

        while (vocable == null) {
            val random = SessionContext.vocables!!.random()
            if (random.id != active.id) {
                val count = stats[random.id]
                if (count != null) {
                    if (count < 3)
                        vocable = random
                } else {
                    vocable = random
                }
            }
        }
        activeVocable = Observable.just(vocable)
    }

    fun stop() {
        SessionContext.vocables = null
        stats.clear()
    }
}


