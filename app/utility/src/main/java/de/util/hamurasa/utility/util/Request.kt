package de.util.hamurasa.utility.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Command<T>(private val commandAction: CommandAction<T>) {

    fun compute(action: () -> T) {
        try {
            val result = action.invoke()
            commandAction.onSuccess(result)
        } catch (exception: Exception) {
            commandAction.onFailure(exception)
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
    crossinline onSuccess: (T) -> Unit = {},
    crossinline onFailure: (exception: Exception) -> Unit = {},
    crossinline action: suspend () -> T
) {
    getCommand(onSuccess, onFailure).computeAsync {
        action.invoke()
    }
}

inline fun <T> getCommand(
    crossinline onSuccess: (T) -> Unit,
    crossinline onFailure: (exception: Exception) -> Unit
): Command<T> {
    return Command(commandAction = object :
        CommandAction<T> {
        override fun onSuccess(value: T) {
            onSuccess.invoke(value)
        }

        override fun onFailure(exception: Exception) {
            onFailure.invoke(exception)
        }

    })
}

interface CommandAction<T> {
    fun onSuccess(value: T)
    fun onFailure(exception: Exception)
}

inline fun <T> request(
    crossinline onSuccess: (T) -> Unit = {},
    crossinline onFailure: (exception: Exception) -> Unit = {},
    crossinline action: () -> T
) {
    getCommand(onSuccess, onFailure).compute {
        action.invoke()
    }
}


