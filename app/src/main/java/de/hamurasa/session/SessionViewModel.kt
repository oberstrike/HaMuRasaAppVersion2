package de.hamurasa.session

import android.content.Context
import de.hamurasa.util.AbstractViewModel
import de.hamurasa.util.SchedulerProvider
import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.session.models.VocableWrapper
import de.hamurasa.settings.SettingsContext
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlin.math.acos

class SessionViewModel(
    val provider: SchedulerProvider,
    val context: Context
) : AbstractViewModel() {


    fun init() {
        val next = SessionContext.vocables.random()
        SessionContext.activeVocable = Observable.just(next)
        SessionContext.running.onNext(true)
        SessionContext.sessionType = SessionType.values().random()

    }

    fun next(correct: Boolean) {
        val vocable = SessionContext.activeVocable.blockingFirst()
        vocable.level += if (correct) 1 else -1
        vocable.attempts += 1


        if (vocable.level >= 6) {
            SessionContext.vocables =
                SessionContext.vocables.filterNot { it.value == vocable.value }
        }

        if (SessionContext.vocables.isNotEmpty()) {
            val next = SessionContext.vocables.random()
            SessionContext.activeVocable = Observable.just(next)
        } else {
            SessionContext.running.onNext(false)
        }
        SessionContext.sessionType = SessionType.values().random()
    }


    fun <T> observe(observable: Observable<T>, action: (value: T) -> Unit) {
        observe(
            observable, provider.computation(), provider.ui(), action
        )
    }
}


