package de.util.hamurasa.utility.main

import androidx.lifecycle.ViewModel
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

interface FlowViewModel {
    val jobs: MutableSet<Deferred<*>>

    fun <T> launchJob(job: suspend () -> T) {
        jobs.add(
            GlobalScope.async(Dispatchers.IO) {
                job()
            }
        )

    }

}

abstract class AbstractViewModel : ViewModel(), RxViewModel, FlowViewModel {
    override val disposables = CompositeDisposable()
    override val jobs: MutableSet<Deferred<*>> = mutableSetOf()


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