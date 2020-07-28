package de.hamurasa.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*

interface RxViewModel {
    val disposables: CompositeDisposable

    fun launchRx(job: () -> Disposable) {
        disposables.add(job())
    }
}


abstract class AbstractViewModel : ViewModel(), RxViewModel {
    override val disposables = CompositeDisposable()
    private val jobs: MutableSet<Deferred<*>> = mutableSetOf()

    fun <T> launchJob(
        coroutineScope: CoroutineScope = viewModelScope,
        job: suspend () -> T
    ) {
        jobs.add(
            coroutineScope.async {
                job()
            }
        )
    }

    public override fun onCleared() {
        jobs.forEach {
            it.cancel()
        }
        jobs.clear()
        disposables.clear()
        super.onCleared()
    }


    inline fun <T> observe(
        observable: Observable<T>,
        subscribeOn: Scheduler,
        observeOn: Scheduler,
        crossinline action: (value: T) -> Unit
    ) {
        launchRx {
            observable
                .subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe { value ->
                    action.invoke(value)
                }
        }
    }


}