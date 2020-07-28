package de.util.hamurasa.utility

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner
import kotlin.coroutines.CoroutineContext

@RunWith(MockitoJUnitRunner::class)
class FlowTest {

    @ExperimentalCoroutinesApi
    @Test
    fun test() = runBlocking {
        val eventHandler = EventHandler()
        val listOfString = mutableListOf<String>()


        val job = GlobalScope.async(Dispatchers.IO) {
            eventHandler.event.collect {
                listOfString.add(it.toString())
            }
        }

        for (i in 0..3) {
            eventHandler.change(Event())
        }

        job.cancel()
        assert(listOfString.size == 5)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun dataClassesTest() = runBlocking {
        val event = Event()
        val copyEvent = event.copy()
        val eventHandler = EventHandler()
        var job: Job = Job()
        val scope = Dispatchers.IO + job




        job = GlobalScope.async(Dispatchers.IO) {
            eventHandler.event.collect {
                println(it)
            }

        }

        eventHandler.change(Event())

        job.join()
        assert(event == copyEvent)
    }


}


data class Event(
    val id: Long = c_id,
    val names: List<String> = listOf()
) {
    companion object {
        var c_id: Long = 0
            get() {
                return field++
            }
    }

}


@ExperimentalCoroutinesApi
class EventHandler {

    private val _events: MutableStateFlow<Event> = MutableStateFlow(Event())

    val event: StateFlow<Event> = _events

    suspend fun change(event: Event) {
        delay(500)
        _events.value = event
    }


}