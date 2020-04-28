package de.hamurasa.util

import de.hamurasa.main.MainViewModel
import de.hamurasa.settings.SettingsContext
import io.reactivex.Observable
import kotlinx.coroutines.*
import okhttp3.internal.EMPTY_REQUEST
import java.lang.Exception
import java.util.concurrent.atomic.AtomicBoolean

inline fun MainViewModel.withOnline(
    observable: Observable<Boolean> = SettingsContext.isOffline,
    remember: Boolean = true,
    crossinline alternative: () -> Unit = {},
    crossinline block: () -> Unit
) {
    val offline = observable.blockingFirst()

    if (!offline) {
        try {
            block.invoke()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    } else {
        //if (remember) RequestCache.add { block.invoke() }
        SettingsContext.isOffline = Observable.just(true)
        alternative.invoke()
    }
}

object RequestCache {
    val jobs: MutableSet<suspend () -> Unit> = mutableSetOf()

    fun add(request: suspend () -> Unit) {
        jobs.add(request)
    }

    suspend fun clear() {
        for (job in jobs) {
            job.invoke()
        }
    }

}