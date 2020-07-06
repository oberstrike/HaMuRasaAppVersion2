package de.hamurasa.util

import de.util.hamurasa.utility.main.AbstractViewModel
import io.reactivex.Observable

abstract class BaseViewModel(private val provider: SchedulerProvider) : AbstractViewModel() {

    fun <T> observe(observable: Observable<T>, action: (value: T) -> Unit) {
        observe(
            observable, provider.computation(), provider.ui(), action
        )
    }


}