package de.util.hamurasa.utility

import de.hamurasa.util.FlowContainerHandler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FlowTest {

    @ExperimentalCoroutinesApi
    @Test
    fun test() = runBlocking {

    }

    @ExperimentalCoroutinesApi
    @Test
    fun dataClassesTest() = runBlocking {

        GlobalScope.launch {
            EventHandler.flow.collect {
                println("Hallo 2 ${it.value.name}")
            }
        }

        val event = Event("Markus")
        EventHandler.change(event)
        EventHandler.change(event)
        EventHandler.change(event)

        delay(5000)
    }


}


data class Event(
    val name: String
)


@ExperimentalCoroutinesApi
object EventHandler : FlowContainerHandler<Event>(
    Event("No Name")
)

