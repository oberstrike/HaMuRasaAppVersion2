package de.util.hamurasa.utility

import io.reactivex.Observable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.rx2.asFlow
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FlowTest {

    @ExperimentalCoroutinesApi
    @Test
    fun test() = runBlocking {
        val eventHandler = EventHandler()

        GlobalScope.launch(Dispatchers.IO) {
            eventHandler.event.collect {
                println(it)
            }
        }

        eventHandler.change(Event.TWO)

        GlobalScope.launch(Dispatchers.IO) {
            eventHandler.event.collect {
                println(it)
            }
        }

        eventHandler.change(Event.ONE)
        println("finished")

    }


}

enum class Event {
    ONE, TWO
}

@ExperimentalCoroutinesApi
class EventHandler {

    private val _events: MutableStateFlow<Event> = MutableStateFlow(Event.ONE)

    val event: StateFlow<Event> get() = _events

    suspend fun change(event: Event) {
        delay(500)
        _events.value = event
    }





}