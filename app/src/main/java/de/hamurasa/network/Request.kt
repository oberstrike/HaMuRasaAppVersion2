package de.hamurasa.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

interface CommandAction<T> {
    suspend fun onSuccess(value: T)
    suspend fun onFailure(exception: Exception) = exception.printStackTrace()
}


class Command<T>(private val commandAction: CommandAction<T>) {

    fun compute(action: () -> T) {
        try {
            val result = action.invoke()
            runBlocking {
                commandAction.onSuccess(result)
            }

        } catch (exception: Exception) {
            runBlocking {
                commandAction.onFailure(exception)
            }

        }
    }

    fun computeAsync(action: suspend () -> T) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = action.invoke()
                commandAction.onSuccess(result)
            } catch (exception: Exception) {
                commandAction.onFailure(exception)
            }
        }

    }
}

inline fun <T> requestAsync(
    crossinline onSuccess: suspend (T) -> Unit = {},
    crossinline onFailure: suspend (exception: Exception) -> Unit = {},
    crossinline action: suspend () -> T
) {
    getCommand(onSuccess, onFailure).computeAsync {
        action.invoke()
    }
}

inline fun <T> getCommand(
    crossinline onSuccess: suspend (T) -> Unit,
    crossinline onFailure: suspend (exception: Exception) -> Unit
): Command<T> {
    return Command(commandAction = object : CommandAction<T> {
        override suspend fun onSuccess(value: T) {
            onSuccess.invoke(value)
        }

        override suspend fun onFailure(exception: Exception) {
            onFailure.invoke(exception)
        }

    })
}

inline fun <T> request(
    crossinline action: () -> T,
    crossinline onSuccess: suspend (T) -> Unit = {},
    crossinline onFailure: suspend (exception: Exception) -> Unit = {}
) {
    getCommand(onSuccess, onFailure).compute {
        action.invoke()
    }
}


