package de.util.hamurasa.utility.main

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class AbstractViewModel : ViewModel() {
    private val disposables = CompositeDisposable()

    fun launch(job: () -> Disposable) {
        disposables.add(job())
    }

    inline fun <T> observe(
        observable: Observable<T>,
        subscribeOn: Scheduler,
        observeOn: Scheduler,
        crossinline action: (value: T) -> Unit
    ) {
        launch {
            observable
                .subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe { value ->
                    action.invoke(value)
                }
        }
    }


    @CallSuper
    public override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }


}