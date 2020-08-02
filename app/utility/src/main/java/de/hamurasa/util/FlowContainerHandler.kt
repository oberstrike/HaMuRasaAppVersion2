package de.hamurasa.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.lang.Exception

interface IFlowHandler<T> {
    val flow: Flow<T>

    suspend fun change(value: T)
    fun value(): T
}

@ExperimentalCoroutinesApi
abstract class FlowContainerHandler<T>(
    startValue: T
) {

    class Container<T>(val value: T)

    private val mutableStateFlow: MutableStateFlow<Container<T>> =
        MutableStateFlow(Container(startValue))

    val flow: StateFlow<Container<T>> = mutableStateFlow

    suspend fun change(value: T) {
        delay(100)
        try {
            mutableStateFlow.value = Container(value)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun value(): T = mutableStateFlow.value.value
}

@ExperimentalCoroutinesApi
abstract class FlowHandler<T>(startValue: T) : IFlowHandler<T> {

    private val mutableStateFlow: MutableStateFlow<T> = MutableStateFlow(startValue)

    override val flow: Flow<T> = mutableStateFlow

    override suspend fun change(value: T) {
        delay(100)
        mutableStateFlow.value = value
    }

    override fun value(): T = mutableStateFlow.value

}